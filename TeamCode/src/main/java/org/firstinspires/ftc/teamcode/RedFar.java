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
        Pose2d initialPose = new Pose2d(0,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);

        Action preload;
        Action wait;
        Action goto1;
        Action pickup1;
        Action launch1;

        preload = drive.actionBuilder(initialPose)
                .lineToY(4)
                .turn(Math.toRadians(-26.56))
            .build();

        wait = drive.actionBuilder(initialPose)
                .waitSeconds(1)
            .build();

        goto1 = drive.actionBuilder(initialPose)
                .turn(Math.toRadians(26.56))
                .lineToY(26)
                .turn(Math.toRadians(90))
            .build();

        pickup1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(46,26))
            .build();

        launch1 = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(0,4))
                .turn(Math.toRadians(-90-26.56))
            .build();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        preload,
                        new ParallelAction(
                                intake.setOutake(0.95),
                                intake.setTrigger(0.49)
                        ),
                        wait,
                        intake.setIntake(0.6),
                        wait,
                        new ParallelAction(
                                intake.setTrigger(0.4),
                                intake.setOutake(0),
                                intake.setIntake(0)
                        ),

                        goto1,
                        new ParallelAction(
                                intake.setIntake(0.8),
                                pickup1
                        ),
                        intake.setIntake(0),
                        launch1,
                        new ParallelAction(
                                intake.setOutake(0.95),
                                intake.setTrigger(0.49)
                        ),
                        wait,
                        intake.setIntake(0.6),
                        wait,
                        new ParallelAction(
                                intake.setTrigger(0.4),
                                intake.setOutake(0),
                                intake.setIntake(0)
                        )

                )
        );

    }
}