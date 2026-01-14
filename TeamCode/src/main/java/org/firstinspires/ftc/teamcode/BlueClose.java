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
public class BlueClose extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(26,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

        Action preload;
        Action goto1;
        Action pickup1;
        Action launch1;
        Action park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0,-50))
                .turn(Math.toRadians(45))
                .build();

        goto1 = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-90+45)))
                .turn(Math.toRadians(-45-90))
                .build();

        pickup1 = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(180)))
                .strafeTo(new Vector2d(-38,-50))
                .build();

        launch1 = drive.actionBuilder(new Pose2d(-38,-50,Math.toRadians(180)))
                .strafeTo(new Vector2d(0,-50))
                .turn(Math.toRadians(90+45))
                .build();

        park = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-90+45)))
                .turn(Math.toRadians(-45))
                .strafeTo(new Vector2d(0,-74))
                .build();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        preload,
                        intake.setTrigger(0.4),
                        intake.setOutake(0.77),
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
                        intake.setOutake(0.75),
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),

                        park

                )
        );

    }
}