package org.firstinspires.ftc.teamcode;

// RoadRunner Specific Imports

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
  Wireless Code Download: Terminal --> "adb connect 192.168.43.1:5555"
 */

@Config
@Autonomous
public class BlueClose extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(-42-12,-10,Math.toRadians(-45));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);
        FalconsTeleOp teleop = new FalconsTeleOp();

        TrajectoryActionBuilder preload, goto1, pickup1, launch1, goto2, pickup2, launch2, park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-12,-48));

        goto1 = preload.endTrajectory().fresh()
                .turn(Math.toRadians(-135));

        pickup1 = goto1.endTrajectory().fresh()
                .strafeTo(new Vector2d(-40-12,-48));

        launch1 = pickup1.endTrajectory().fresh()
                .strafeTo(new Vector2d(-12,-48))
                .turn(Math.toRadians(135));

        goto2 = launch1.endTrajectory().fresh()
                .turn(Math.toRadians(-135))
                .strafeTo(new Vector2d(-10-12,-21)); //y should be -72

        pickup2 = goto2.endTrajectory().fresh()
                .strafeTo(new Vector2d(-52-12,-21));

        launch2 = pickup2.endTrajectory().fresh()
                .strafeTo(new Vector2d(-40-12,-21))
                .strafeTo(new Vector2d(-12,-48))
                .turn(Math.toRadians(134.5));

        park = launch2.endTrajectory().fresh()
                .turn(Math.toRadians(-44.5))
                .strafeTo(new Vector2d(-12,-72));



        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                preload.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(0.9),
                                        //intake.setOutake(0.784)
                                        intake.setOutakeVelocity(1530)
                                )
                        ),
                        new SleepAction(0.1), //1.25
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                //intake.setOutake(0),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        goto1.build(),
                        intake.setIntake(0.8),
                        pickup1.build(),
                        intake.setIntake(0),

                        /*launch1,
                        intake.setIntake(-0.4),
                        new SleepAction(0.15),
                        intake.setIntake(0),
                        intake.setTrigger(0.4),
                        intake.setOutake(0.75),
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0), */

                        new ParallelAction(
                                launch1.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(2.2),
                                        //intake.setOutake(0.784)
                                        intake.setOutakeVelocity(1530)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.12),
                                        intake.setIntake(0)
                                )
                        ),
                        new SleepAction(0.1), //1.25
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                //intake.setOutake(0),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        goto2.build(),
                        intake.setIntake(0.8),
                        pickup2.build(),
                        intake.setIntake(0),

                        new ParallelAction(
                                launch2.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(3.6),
                                        //intake.setOutake(0.782)
                                        intake.setOutakeVelocity(1530)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.1),
                                        intake.setIntake(0)
                                )
                        ),
                        new SleepAction(0.1), //1.25
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                //intake.setOutake(0),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        /*launch2,
                        intake.setIntake(-0.4),
                        new SleepAction(0.15),
                        intake.setIntake(0),
                        intake.setTrigger(0.4),
                        intake.setOutake(0.75),
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),*/

                        park.build()

                )
        );
        //teleop.initialPose = new Pose2d(new Vector2d(-12 ,-72), Math.toRadians(-90));
        //MecanumDrive.PARAMS.blueRun = true;

    }
}