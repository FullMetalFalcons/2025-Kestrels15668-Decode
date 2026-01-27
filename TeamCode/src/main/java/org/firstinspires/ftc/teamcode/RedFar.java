package org.firstinspires.ftc.teamcode;

// RoadRunner Specific Imports

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
  Wireless Code Download: Terminal --> "adb connect 192.168.43.1:5555"
 */

@Config
@Autonomous
public class RedFar extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(12,-62,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);
        FalconsTeleOp teleop = new FalconsTeleOp();

        Action preload;
        Action goto1;
        Action pickup1;
        Action launch1;

        Action park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(12,4-62))
                .turn(Math.toRadians(-23.5))
                //.strafeToLinearHeading(new Vector2d(0,4),Math.toRadians(-90-24))
            .build();

        goto1 = drive.actionBuilder(new Pose2d(12,4-62,Math.toRadians(-90-23.5)))
                .turn(Math.toRadians(23.5))
                .strafeTo(new Vector2d(12,24-62))
                .turn(Math.toRadians(90))
                //.strafeToLinearHeading(new Vector2d(12,26),Math.toRadians(0))
            .build();

        pickup1 = drive.actionBuilder(new Pose2d(12,24-62,Math.toRadians(0)))
                .strafeTo(new Vector2d(52+12,24-62))
            .build();

        launch1 = drive.actionBuilder(new Pose2d(52+12,24-62,Math.toRadians(0)))
                .strafeTo(new Vector2d(12,24+24-62))
                .turn(Math.toRadians(-90-23.5))
                //.strafeToLinearHeading(new Vector2d(0,4),Math.toRadians(-90-23))
            .build();

        park = drive.actionBuilder(new Pose2d(12,48-62,Math.toRadians(-90-23.5)))
                .turn(Math.toRadians(23.5))
                .strafeTo(new Vector2d(12,48+24-62))
                //.strafeToLinearHeading(new Vector2d(0,24),Math.toRadians(-90))
            .build();



        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        /*preload,
                        intake.setTrigger(0.4),
                        intake.setOutake(0.96), //13.55V
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),*/

                        new ParallelAction(
                                preload,
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(0),
                                        //intake.setOutake(0.972)
                                        intake.setOutakeVelocity(2150)
                                )
                        ),
                        new SleepAction(0.15), //1.25
                        intake.setIntake(0.57),
                        new SleepAction(1.55),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                //intake.setOutake(0),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        goto1,
                        intake.setIntake(0.8),
                        new SleepAction(0.1),
                        pickup1,
                        intake.setIntake(0),

                        new ParallelAction(
                                launch1,
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        intake.setOutake(-0.1),
                                        new SleepAction(0.5),
                                        intake.setOutake(0),
                                        new SleepAction(2.4),
                                        //intake.setOutake(0.972)
                                        intake.setOutakeVelocity(2150)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.15),
                                        intake.setIntake(0)
                                )
                        ),
                        new SleepAction(0.5), //1.25
                        intake.setIntake(0.7),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                //intake.setOutake(0),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        /*launch1,
                        intake.setIntake(-0.4),
                        new SleepAction(0.15),
                        intake.setIntake(0),
                        intake.setTrigger(0.4),
                        intake.setOutake(0.96),
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),
                        new SleepAction(2),*/

                        park
                )
        );
        //teleop.initialPose = new Pose2d(new Vector2d(12 ,50+24-62), Math.toRadians(-90));
        //MecanumDrive.PARAMS.blueRun = false;



    }
}