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
public class BlueFarExperimental extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(0,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

        Action preload;
        Action gotoHP;
        Action pickupHP;
        Action launchHP;
        Action goto1;
        Action pickup1;
        Action launch1;
        Action park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0,4))
                .turn(Math.toRadians(24))
            .build();

        gotoHP = drive.actionBuilder(new Pose2d(0,4,Math.toRadians(-90+24)))
                .turn(Math.toRadians(-24-90))
                .build();

        pickupHP = drive.actionBuilder(new Pose2d(0,4,Math.toRadians(180)))
                .strafeTo(new Vector2d(-38,4))
                .build();

        launchHP = drive.actionBuilder(new Pose2d(-38,4,Math.toRadians(180)))
                .strafeTo(new Vector2d(0,4))
                .turn(Math.toRadians(90+23))
                .build();

        goto1 = drive.actionBuilder(new Pose2d(0,4,Math.toRadians(-90+24)))
                .turn(Math.toRadians(-24))
                .waitSeconds(.25)
                .strafeTo(new Vector2d(0,28))
                .turn(Math.toRadians(-90))
            .build();

        pickup1 = drive.actionBuilder(new Pose2d(0,28,Math.toRadians(180)))
                .strafeTo(new Vector2d(-38,28))
            .build();

        launch1 = drive.actionBuilder(new Pose2d(-38,28,Math.toRadians(180)))
                .strafeTo(new Vector2d(0,24+28))
                .turn(Math.toRadians(90+23))
            .build();

        park = drive.actionBuilder(new Pose2d(0,52,Math.toRadians(-90+24)))
                .turn(Math.toRadians(-23))
                .strafeTo(new Vector2d(0,24+52))
            .build();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        preload,
                        intake.setTrigger(0.4),
                        intake.setOutake(0.955), //13.55V
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),

                        gotoHP,
                        intake.setIntake(0.8),
                        pickupHP,
                        intake.setIntake(0),

                        launchHP,
                        intake.setIntake(-0.4),
                        new SleepAction(0.1),
                        intake.setIntake(0),
                        intake.setTrigger(0.4),
                        intake.setOutake(0.955),
                        new SleepAction(1.25),
                        intake.setIntake(0.6),
                        new SleepAction(1.5),
                        intake.setTrigger(0.49),
                        intake.setOutake(0),
                        intake.setIntake(0),
                        new SleepAction(2),

                        goto1,
                        intake.setIntake(0.8),
                        pickup1,
                        intake.setIntake(0),

                        launch1,
                        intake.setIntake(-0.4),
                        new SleepAction(0.1),
                        intake.setIntake(0),
                        intake.setTrigger(0.4),
                        intake.setOutake(0.955),
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