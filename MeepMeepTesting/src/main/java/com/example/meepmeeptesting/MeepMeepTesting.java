package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 16)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, -38, Math.toRadians(-90)))
                .waitSeconds(2)
                .lineToY(4-38)
                .turn(Math.toRadians(-26.56))
                .waitSeconds(1)
                .turn(Math.toRadians(26.56))
                .lineToY(26-38)
                .turn(Math.toRadians(90))
                .strafeTo(new Vector2d(50+12,26-38))
                .waitSeconds(1)
                .strafeTo(new Vector2d(0+12,4-38))
                .turn(Math.toRadians(-90-26.56))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}