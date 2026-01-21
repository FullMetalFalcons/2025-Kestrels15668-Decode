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
public class BlueClosePark extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(26,-4,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

        Action preload;
        Action goto1;
        Action park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0,-50))
                .turn(Math.toRadians(45.5))
                .build();

        park = drive.actionBuilder(new Pose2d(0,-50,Math.toRadians(-90+45.5)))
                .turn(Math.toRadians(-45.5))
                .strafeTo(new Vector2d(0,-26))
                .strafeTo(new Vector2d(40,-26))
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

                        park

                )
        );

    }
}