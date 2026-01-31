package org.firstinspires.ftc.teamcode;

// RoadRunner Specific Imports

import com.acmerobotics.dashboard.config.Config;
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
public class BlueFarHP extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(-12,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);
        FalconsTeleOp teleop = new FalconsTeleOp();

        TrajectoryActionBuilder preload, gotoHP, pickupHP, launchHP, park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-12,4))
                .turn(Math.toRadians(24.5));

        /*gotoHP = preload.endTrajectory().fresh()
                .turn(Math.toRadians(24.5))
                .strafeTo(new Vector2d(12,12)) //12
                .turn(Math.toRadians(90-15));

        pickupHP = gotoHP.endTrajectory().fresh()
                .strafeTo(new Vector2d(42+12,12-22))
                .waitSeconds(0.2)
                .strafeTo(new Vector2d(40+12,19-20));

        launchHP = pickupHP.endTrajectory().fresh()
                .strafeTo(new Vector2d(12,24)) // -12,4
                .turn(Math.toRadians(-90+15-24.5));*/

        park = preload.endTrajectory().fresh()
                .turn(Math.toRadians(-24.5))
                .strafeTo(new Vector2d(12,4));


        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                preload.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(0),
                                        intake.setOutakeVelocity(1940)
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
                        park.build()
                )
        );
        //teleop.initialPose = new Pose2d(new Vector2d(12 ,50+24-62), Math.toRadians(-90));
        //MecanumDrive.PARAMS.blueRun = false;



    }
}