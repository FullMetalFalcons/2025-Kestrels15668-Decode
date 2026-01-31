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
public class RedFar extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(12,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);
        FalconsTeleOp teleop = new FalconsTeleOp();

        TrajectoryActionBuilder preload, goto1, pickup1, launch1, park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(12,4))
                .turn(Math.toRadians(-23.5));

        goto1 = preload.endTrajectory().fresh()
                .turn(Math.toRadians(23.5))
                .strafeTo(new Vector2d(12,24))
                .turn(Math.toRadians(90));

        pickup1 = goto1.endTrajectory().fresh()
                .strafeTo(new Vector2d(49+12,24));

        launch1 = pickup1.endTrajectory().fresh()
                .strafeTo(new Vector2d(12,44))
                .turn(Math.toRadians(-90-23.5));

        park = launch1.endTrajectory().fresh()
                .turn(Math.toRadians(23.5))
                .strafeTo(new Vector2d(12,72));



        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                preload.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(0),
                                        intake.setOutakeVelocity(2140)
                                )
                        ),
                        new SleepAction(0.155),
                        intake.setIntake(0.57),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        goto1.build(),
                        intake.setIntake(0.8),
                        pickup1.build(),
                        new SleepAction(0.1),
                        intake.setIntake(0),

                        new ParallelAction(
                                launch1.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        intake.setOutake(-0.1),
                                        new SleepAction(0.5),
                                        intake.setOutake(0),
                                        new SleepAction(2.2),
                                        intake.setOutakeVelocity(2140)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.15),
                                        intake.setIntake(0)
                                )
                        ),
                        new SleepAction(0.5),
                        intake.setIntake(0.57),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                intake.setOutakeVelocity(0),
                                intake.setIntake(0)
                        ),

                        park.build()
                )
        );
        //teleop.initialPose = new Pose2d(new Vector2d(12 ,50+24), Math.toRadians(-90));
        //MecanumDrive.PARAMS.blueRun = false;



    }
}