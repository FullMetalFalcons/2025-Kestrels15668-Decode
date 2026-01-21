package org.firstinspires.ftc.teamcode;

// RoadRunner Specific Imports

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
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
        Pose2d initialPose = new Pose2d(0,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

        Action preload;
        Action goto1;
        Action pickup1;
        Action launch1;
        Action park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0,4))
                .turn(Math.toRadians(-24))
                //.strafeToLinearHeading(new Vector2d(0,4),Math.toRadians(-90-24))
            .build();

        goto1 = drive.actionBuilder(new Pose2d(0,4,Math.toRadians(-90-24)))
                .turn(Math.toRadians(24))
                .strafeTo(new Vector2d(0,26))
                .turn(Math.toRadians(90))
                //.strafeToLinearHeading(new Vector2d(12,26),Math.toRadians(0))
            .build();

        pickup1 = drive.actionBuilder(new Pose2d(12,26,Math.toRadians(0)))
                .strafeTo(new Vector2d(38,26))
            .build();

        launch1 = drive.actionBuilder(new Pose2d(38,26,Math.toRadians(0)))
                .strafeTo(new Vector2d(0,24+26))
                .turn(Math.toRadians(-90-23))
                //.strafeToLinearHeading(new Vector2d(0,4),Math.toRadians(-90-23))
            .build();

        park = drive.actionBuilder(new Pose2d(0,4,Math.toRadians(-90-23)))
                .turn(Math.toRadians(23))
                .strafeTo(new Vector2d(0,24+50))
                //.strafeToLinearHeading(new Vector2d(0,24),Math.toRadians(-90))
            .build();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        preload,
                        intake.setTrigger(0.4),
                        intake.setOutake(0.957), //13.55V
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),

                        goto1,
                        intake.setIntake(0.8),
                        pickup1,
                        intake.setIntake(0),

                        launch1,
                        intake.setIntake(-0.4),
                        new SleepAction(0.15),
                        intake.setIntake(0),
                        intake.setTrigger(0.4),
                        intake.setOutake(0.957),
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),
                        new SleepAction(2),

                        park
                )
        );

    }
}