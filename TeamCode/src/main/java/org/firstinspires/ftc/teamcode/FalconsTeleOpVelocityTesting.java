package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;


@TeleOp
public class FalconsTeleOpVelocityTesting extends LinearOpMode {
    //Action blueClose,blueFar,redClose,redFar;
    //Initialize motors, servos, sensors, imus, etc.
    DcMotorEx motorLF, motorRF, motorLB, motorRB, motorLaunch, motorRamp1, motorRamp2, motorIntake;
    Servo servoTrigger, lightLauncher;
    VoltageSensor voltageSensor;
    //public Pose2d initialPose;
    double reverse, voltage;
    boolean lastB, lastA, launchRunFar, launchRunClose, lastRB, intakeRun/*,blueRunNow*/;

    public static MecanumDrive.Params DRIVE_PARAMS = new MecanumDrive.Params();
    KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

    // The following code will run as soon as "INIT" is pressed on the Driver Station
    public void runOpMode() {


        //Define those motors and stuff
        //The string should be the name on the Driver Hub
        // Set the strings at the top of the MecanumDrive file; they are shared between TeleOp and Autonomous

        motorLF = (DcMotorEx) hardwareMap.dcMotor.get(DRIVE_PARAMS.leftFrontDriveName);
        motorLB = (DcMotorEx) hardwareMap.dcMotor.get(DRIVE_PARAMS.leftBackDriveName);
        motorRF = (DcMotorEx) hardwareMap.dcMotor.get(DRIVE_PARAMS.rightFrontDriveName);
        motorRB = (DcMotorEx) hardwareMap.dcMotor.get(DRIVE_PARAMS.rightBackDriveName);

        motorLaunch = (DcMotorEx) hardwareMap.dcMotor.get("Launch1");
        motorRamp1 = (DcMotorEx) hardwareMap.dcMotor.get("Intake1");
        motorRamp2 = (DcMotorEx) hardwareMap.dcMotor.get("Intake2");
        motorIntake = (DcMotorEx) hardwareMap.dcMotor.get("intake");

        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        // Use the following line as a template for defining new servos
        //Claw = (Servo) hardwareMap.servo.get("claw");
        servoTrigger = (Servo) hardwareMap.servo.get("trigga");
        lightLauncher = (Servo) hardwareMap.servo.get("light");


        //Set them to the correct modes
        //This reverses the motor direction
        // This data is also set at the top of MecanumDrive, for the same reasons as above
        motorLF.setDirection(DRIVE_PARAMS.leftFrontDriveDirection);
        motorLB.setDirection(DRIVE_PARAMS.leftBackDriveDirection);
        motorRF.setDirection(DRIVE_PARAMS.rightFrontDriveDirection);
        motorRB.setDirection(DRIVE_PARAMS.rightBackDriveDirection);
        motorLaunch.setDirection(DcMotorSimple.Direction.REVERSE);
        motorIntake.setDirection(DcMotorSimple.Direction.REVERSE);


        //This resets the encoder values when the code is initialized
        motorLF.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorLB.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorRF.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorRB.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        //This makes the wheels tense up and stay in position when it is not moving, opposite is FLOAT
        motorLF.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motorLaunch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //This lets you look at encoder values while the OpMode is active
        //If you have a STOP_AND_RESET_ENCODER, make sure to put this below it
        motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLaunch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorLaunch.setVelocityPIDFCoefficients(0, 0, 0, 5);
        //Initial Pose for
        /*MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        blueClose = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-12,12),Math.toRadians(-45))
                .build();
        blueFar = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-12,-58),Math.toRadians(-24-90))
                .build();
        redClose = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(12,12),Math.toRadians(-135))
                .build();
        redFar = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(12,-58),Math.toRadians(24-90))
                .build();

        blueRunNow = MecanumDrive.PARAMS.blueRun;*/

        // The program will pause here until the Play icon is pressed on the Driver Station
        waitForStart();

        // opModeIsActive() returns "true" as long as the Stop button has not been pressed on the Driver Station
        while(opModeIsActive()) {

            // Mecanum drive code
            double powerX = 0.0;  // Desired power for strafing           (-1 to 1)
            double powerY = 0.0;  // Desired power for forward/backward   (-1 to 1)
            double powerAng = 0.0;  // Desired power for turning          (-1 to 1)

            // Set the desired powers based on joystick inputs (-1 to 1)
            powerX = gamepad1.left_stick_x * reverse;
            powerY = -gamepad1.left_stick_y * reverse;
            powerAng = -gamepad1.right_stick_x;

            // Perform vector math to determine the desired powers for each wheel
            double powerLF = powerX + powerY - powerAng;
            double powerLB = -powerX + powerY - powerAng;
            double powerRF = -powerX + powerY + powerAng;
            double powerRB = powerX + powerY + powerAng;

            // Determine the greatest wheel power and set it to max
            double max = Math.max(1.0, Math.abs(powerLF));
            max = Math.max(max, Math.abs(powerRF));
            max = Math.max(max, Math.abs(powerLB));
            max = Math.max(max, Math.abs(powerRB));

            // Scale all power variables down to a number between 0 and 1 (so that setPower will accept them)
            powerLF /= max;
            powerLB /= max;
            powerRF /= max;
            powerRB /= max;

            motorLF.setPower(powerLF);
            motorLB.setPower(powerLB);
            motorRF.setPower(powerRF);
            motorRB.setPower(powerRB);



            if (gamepad1.right_trigger > 0.25) {
                reverse = -1;
            } else {
                reverse = 1;
            }



            /*if (gamepad2.b && !lastB) {
                if (!launchRunFar) {
                    motorLaunch.setPower(0.955*13.5/voltage);
                    launchRunFar = true;
                } else {
                    motorLaunch.setPower(0);
                    launchRunFar = false;
                }
            } else if (gamepad2.a && !lastA) {
                if (!launchRunClose) {
                    motorLaunch.setPower(0.7*13.5/voltage);
                    launchRunClose = true;
                } else {
                    motorLaunch.setPower(0);
                    launchRunClose = false;
                }
            }
            lastB = gamepad2.b;
            lastA = gamepad2.a; */

            voltage = voltageSensor.getVoltage();

            if (gamepad2.b) {
                motorLaunch.setVelocity(2200);
            } else if (gamepad2.a) {
                motorLaunch.setVelocity(1800);
            } else {
                motorLaunch.setPower(0);
            }

            if (gamepad1.right_bumper && !lastRB) {
                intakeRun = !intakeRun;
            }

            lastRB = gamepad1.right_bumper;

            if (intakeRun) {
                motorRamp1.setPower(0.8);
                motorRamp2.setPower(-0.8);
                motorIntake.setPower(1);
            } else if (gamepad2.right_bumper) {
                motorRamp1.setPower(0.6);
                motorRamp2.setPower(-0.6);
                motorIntake.setPower(1);
            } else if (gamepad1.left_bumper || gamepad2.left_bumper) {
                motorRamp1.setPower(-0.55);
                motorRamp2.setPower(0.55);
                motorIntake.setPower(1);
            } else {
                motorRamp1.setPower(0);
                motorRamp2.setPower(0);
                motorIntake.setPower(0);
            }
            if (gamepad2.right_trigger > 0.25) {
                servoTrigger.setPosition(0.4);
            } else {
                servoTrigger.setPosition(0.48);
            }

            if (motorLaunch.getVelocity() > 1600 && gamepad2.b) {
                lightLauncher.setPosition(0.611);
            } else if (gamepad2.b /*|| (gamepad2.b && (gamepad1.left_stick_x > 0.1 || gamepad1.left_stick_y > 0.1 || gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_y > 0.1))*/) {
                lightLauncher.setPosition(0.279);
            } else if (motorLaunch.getVelocity() > 1550 && gamepad2.a) {
                lightLauncher.setPosition(0.611);
            } else if (gamepad2.a /*|| (gamepad2.a && (gamepad1.left_stick_x > 0.1 || gamepad1.left_stick_y > 0.1 || gamepad1.right_stick_x > 0.1 || gamepad1.right_stick_y > 0.1)))*/) {
                lightLauncher.setPosition(0.279);
            } else {
                lightLauncher.setPosition(0);
            }

            if (gamepad2.dpad_down) {
                Actions.runBlocking(
                        new SequentialAction(
                                new ParallelAction(
                                        intake.setTrigger(0.4),
                                        intake.setOutake(0.784),
                                        new SequentialAction(
                                                intake.setIntake(-0.4),
                                                new SleepAction(0.12),
                                                intake.setIntake(0)
                                        )
                                ),
                                new SleepAction(1.25),
                                intake.setIntake(0.6),
                                new SleepAction(1.5),
                                new ParallelAction(
                                        intake.setIntake(0),
                                        intake.setOutake(0),
                                        intake.setTrigger(0)
                                )
                        )
                );
            }

            if (gamepad2.dpad_right) {
                Actions.runBlocking(
                        new SequentialAction(
                                new ParallelAction(
                                        intake.setTrigger(0.4),
                                        intake.setOutake(0.972),
                                        new SequentialAction(
                                                intake.setIntake(-0.4),
                                                new SleepAction(0.12),
                                                intake.setIntake(0)
                                        )
                                ),
                                new SleepAction(1.25),
                                intake.setIntake(0.6),
                                new SleepAction(1.5),
                                new ParallelAction(
                                        intake.setIntake(0),
                                        intake.setOutake(0),
                                        intake.setTrigger(0)
                                )
                        )
                );
            }

            // If you want to print information to the Driver Station, use telemetry
            // addData() lets you give a string which is automatically followed by a ":" when printed
            //     the variable that you list after the comma will be displayed next to the label
            // update() only needs to be run once and will "push" all of the added data

            /*if (blueRunNow && gamepad2.dpad_down) {
                    //Blue close
                Actions.runBlocking(
                   blueClose
                );
            } else if (blueRunNow && gamepad2.dpad_right) {
                    //Blue far
                Actions.runBlocking(
                    blueFar
                );
            } else if (!blueRunNow && gamepad2.dpad_down) {
                    //Red close
                Actions.runBlocking(
                    redClose
                );
            } else if (!blueRunNow && gamepad2.dpad_right) {
                    //Red far
                Actions.runBlocking(
                    redFar
                );
            }*/

                telemetry.addData("servoPosition", servoTrigger.getPosition());
            telemetry.addData("voltage", voltage);
            telemetry.addData("launchRPM", motorLaunch.getVelocity());

            telemetry.update();

        } // opModeActive loop ends
    }
} // end class