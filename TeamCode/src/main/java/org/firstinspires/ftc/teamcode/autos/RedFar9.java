package org.firstinspires.ftc.teamcode.autos;

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

import org.firstinspires.ftc.teamcode.FalconsTeleOp;
import org.firstinspires.ftc.teamcode.KestrelIntake;
import org.firstinspires.ftc.teamcode.MecanumDrive;

/*
  Wireless Code Download: Terminal --> "adb connect 192.168.43.1:5555"
 */

@Config
@Autonomous
public class RedFar9 extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(12,0,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);
        FalconsTeleOp teleop = new FalconsTeleOp();

        TrajectoryActionBuilder preload, goto1, pickup1, launch1, gotoHP, pickupHP, launchHP, park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(12,4))
                .turn(Math.toRadians(-24.5));

        goto1 = preload.endTrajectory().fresh()
                .turn(Math.toRadians(24.5))
                .strafeTo(new Vector2d(12,24))
                .turn(Math.toRadians(90));

        pickup1 = goto1.endTrajectory().fresh()
                .strafeTo(new Vector2d(48+12,24));

        launch1 = pickup1.endTrajectory().fresh()
                .strafeTo(new Vector2d(12,48-4))
                .turn(Math.toRadians(-90-24.5));

        gotoHP = launch1.endTrajectory().fresh()
                .turn(Math.toRadians(24.5))
                .strafeTo(new Vector2d(12,48+12)) //12
                .turn(Math.toRadians(90-15));

        pickupHP = gotoHP.endTrajectory().fresh()
                .strafeTo(new Vector2d(50+12,48))
                .waitSeconds(0.2)
                .strafeTo(new Vector2d(51+12,48+6));

        launchHP = pickupHP.endTrajectory().fresh()
                .strafeTo(new Vector2d(12+9,48+25)) // -12,4
                .turn(Math.toRadians(-90+15-24.5));

        park = launchHP.endTrajectory().fresh()
                .turn(Math.toRadians(24.5))
                .strafeTo(new Vector2d(12+9,48+25+24));


        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                preload.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(0),
                                        intake.setOutakeVelocity(1920)
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
                        intake.setIntake(0),

                        new ParallelAction(
                                launch1.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        intake.setOutake(-0.1),
                                        new SleepAction(0.5),
                                        intake.setOutake(0),
                                        new SleepAction(2.2),
                                        intake.setOutakeVelocity(1920)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.2),
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

                        gotoHP.build(),
                        intake.setIntake(0.8),
                        pickupHP.build(),
                        new SleepAction(1.5),

                        new ParallelAction(
                                launchHP.build(),
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        intake.setOutake(-0.1),
                                        new SleepAction(0.5),
                                        intake.setOutake(0),
                                        new SleepAction(1.9),
                                        intake.setOutakeVelocity(1920)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.25),
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
        //teleop.initialPose = new Pose2d(new Vector2d(12 ,50+24-62), Math.toRadians(-90));
        //MecanumDrive.PARAMS.blueRun = false;



    }
}