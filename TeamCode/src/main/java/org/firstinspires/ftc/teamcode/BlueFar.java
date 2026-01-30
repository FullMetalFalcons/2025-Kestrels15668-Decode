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
import com.qualcomm.robotcore.hardware.VoltageSensor;

/*
  Wireless Code Download: Terminal --> "adb connect 192.168.43.1:5555"
 */

@Config
@Autonomous
public class BlueFar extends LinearOpMode {
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(-12,-62,Math.toRadians(-90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        KestrelIntake intake = new KestrelIntake(hardwareMap, telemetry);
        FalconsTeleOp teleop = new FalconsTeleOp();

        /* TODO:  I looked at the RoadRunner docs today and have 2 theories that could help your autonomous...

            1) I think your initial headings could be wrong for paths "pickup1" and onwards. At least on paper, Math.toRadians(180) is not equivalent to Math.toRadians(-180), although
                I feel like that shouldn't make a difference in practice. It's possible that the robot thinks its flipped around to the point where everything works fine, except coordinates
                become mirrored or rotated. Changing those numbers (see my comments below) might help, but at the same time I don't know that it will


            2) Instead of declaring "preload" as an Action, the docs seem to suggest that you create it as a TrajectoryActionBuilder. That will cause a section of code to turn red
                and start complaining, but you can fix that just by removing ".build()" from the end of the block. If you convert "goto1", "pickup1", and "launch1" into TrajectoryActionBuilders
                in the same manner, then you should be able to reference   https://rr.brott.dev/docs/v1-0/guides/centerstage-auto/   to see how to use ".endTrajectory().fresh()" instead of needing
                to reference the ending pose of the previous path. The only other thing that you should need to change if you do that is add in ".build()" down below when you reference these paths
                in the SequentialAction section (see my comment on line 104)

                Note that because "park" is the last trajectory you run, I don't think there is any reason to make it a TrajectoryActionBuilder instead of an Action
                (just change the starting pose to use the ".fresh()" method;  reference the line defining "trajectoryActionCloseOut" in the sample auto page linked above)

                The reason you have to move around ".build()" is that ".build()" converts a TrajectoryActionBuilder into a regular Action. You can't call ".build()" when you
                set up the paths initially, because then you would be trying to give a variable of type TrajectoryActionBuilder a value of type Action, and that doesn't work.
                But "Actions.runBlocking(new SequentialAction())" can only run Actions, so you need to call ".build()" down there to convert the TrajectoryActionBuilders into Actions

                Hopefully that made some amount of sense, but if it doesn't, ask Mr. Tompkins and he might understand what I'm trying to say. None of this may work, but it's my best guess at the moment
         */

        Action preload;
        Action goto1;
        Action pickup1;
        Action launch1;
        Action park;

        preload = drive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(-12,4-62))
                .turn(Math.toRadians(23.5))
                //.strafeToLinearHeading(new Vector2d(0,4),Math.toRadians(-90+24))
                .build();

        goto1 = drive.actionBuilder(new Pose2d(-12,4-62,Math.toRadians(-90+23.5)))
                .turn(Math.toRadians(-23.5))
                .strafeTo(new Vector2d(-12,24-62))
                .turn(Math.toRadians(-90))
                //.strafeToLinearHeading(new Vector2d(-12,26),Math.toRadians(-24))
                .build();

        pickup1 = drive.actionBuilder(new Pose2d(-12,24-62,Math.toRadians(180))) // TODO: Change 180 to -180?
                .strafeTo(new Vector2d(-52-12,24-62))
                .build();

        launch1 = drive.actionBuilder(new Pose2d(-52-12,24-62,Math.toRadians(180))) // TODO: Change 180 to -180?
                .strafeTo(new Vector2d(-12-3,24+24+2-62))
                .turn(Math.toRadians(90+23.5+1))
                //.strafeToLinearHeading(new Vector2d(0,4),Math.toRadians(23))
                .build();

        park = drive.actionBuilder(new Pose2d(-12-3,48+2-62,Math.toRadians(-90+23.5+1)))  // TODO: Change to (180-90+23.5+1)?
                .turn(Math.toRadians(-23.5-1))
                .strafeTo(new Vector2d(-12-3,48+24+2-62))
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
                                preload,                   // TODO: Change to "preload.build()" if following my 2nd suggestion. Do this for all the other paths too, except for "park"
                                intake.setTrigger(0.4),
                                new SequentialAction(
                                        new SleepAction(0),
                                        //intake.setOutake(0.972)
                                        intake.setOutakeVelocity(2140)
                                )
                        ),
                        new SleepAction(0.155), //1.25
                        intake.setIntake(0.57),
                        new SleepAction(1.5),
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
                                        new SleepAction(2.2),
                                        //intake.setOutake(0.972)
                                        intake.setOutakeVelocity(2140)
                                ),
                                new SequentialAction(
                                        intake.setIntake(-0.4),
                                        new SleepAction(0.15),
                                        intake.setIntake(0)
                                )
                        ),
                        new SleepAction(0.5), //1.25
                        intake.setIntake(0.57),
                        new SleepAction(1.5),
                        new ParallelAction(
                                intake.setTrigger(0.48),
                                //
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
        //teleop.initialPose = new Pose2d(new Vector2d(-12 ,50+24-62), Math.toRadians(-90));
        //MecanumDrive.PARAMS.blueRun = true;


    }
}