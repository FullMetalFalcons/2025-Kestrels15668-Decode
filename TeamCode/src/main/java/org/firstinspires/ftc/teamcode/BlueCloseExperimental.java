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
public class BlueCloseExperimental extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(26,-3,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

        Action preload;
        Action goto1;
        Action pickup1;
        Action launch1;
        Action goto2;
        Action pickup2;
        Action launch2;
        Action goto3;
        Action launch3;
        Action park;

        preload = drive.actionBuilder(initialPose)
                //.strafeTo(new Vector2d(0,-50))
                //.turn(Math.toRadians(45))
                .strafeToLinearHeading(new Vector2d(0,-50),Math.toRadians(-45))
                .build();

        goto1 = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-45)))
                //.turn(Math.toRadians(-135))
                .strafeToLinearHeading(new Vector2d(-12,-50),Math.toRadians(-180))
                .build();

        pickup1 = drive.actionBuilder(new Pose2d(-12,-50,Math.toRadians(-180)))
                .strafeTo(new Vector2d(-38,-50))
                .build();

        launch1 = drive.actionBuilder(new Pose2d(-38,-50,Math.toRadians(-180)))
                //.strafeTo(new Vector2d(0,-50))
                //.turn(Math.toRadians(90+45))
                .strafeToLinearHeading(new Vector2d(0,-50),Math.toRadians(-45))
                .build();

        goto2 = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-45)))
                //.turn(Math.toRadians(-135))
                .strafeToLinearHeading(new Vector2d(-12,-74),Math.toRadians(-180))
                .build();

        pickup2 = drive.actionBuilder(new Pose2d(-12,-74,Math.toRadians(-180)))
                .strafeTo(new Vector2d(-38,-74))
                .build();

        launch2 = drive.actionBuilder(new Pose2d(-38,-74,Math.toRadians(-180)))
                //.strafeTo(new Vector2d(0,-50))
                //.turn(Math.toRadians(90+45))
                .strafeToLinearHeading(new Vector2d(0,-50),Math.toRadians(-45))
                .build();

        goto3 = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-45)))
                //.turn(Math.toRadians(-135))
                .strafeToLinearHeading(new Vector2d(-12,-74),Math.toRadians(-180))
                .strafeToLinearHeading(new Vector2d(-48,-62),Math.toRadians(-225))
                .build();

        launch3 = drive.actionBuilder(new Pose2d(-48,-62,Math.toRadians(-225)))
                //.strafeTo(new Vector2d(0,-50))
                //.turn(Math.toRadians(90+45))
                .strafeToLinearHeading(new Vector2d(0,-50),Math.toRadians(-45))
                .build();

        park = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-45)))
                //.turn(Math.toRadians(-45))
                //.strafeTo(new Vector2d(0,-74))
                .strafeToLinearHeading(new Vector2d(0,-74),Math.toRadians(-90))
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

                        goto2,
                        intake.setIntake(0.8),
                        pickup2,
                        intake.setIntake(0),

                        launch2,
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

                        goto3,
                        intake.setIntake(0.8),
                        new SleepAction(2),
                        intake.setIntake(0),

                        launch3,
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