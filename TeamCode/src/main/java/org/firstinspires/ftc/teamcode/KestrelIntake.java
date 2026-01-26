package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class KestrelIntake {

    public DcMotorEx motorLaunch, motorRamp1, motorRamp2, motorIntake;
    public Servo servoTrigger;
    public VoltageSensor voltageSensor;
    double voltage;

    public KestrelIntake(HardwareMap hardwareMap, Telemetry telemetry) {
        motorLaunch = (DcMotorEx) hardwareMap.dcMotor.get("Launch1");
        motorRamp1 = (DcMotorEx) hardwareMap.dcMotor.get("Intake1");
        motorRamp2 = (DcMotorEx) hardwareMap.dcMotor.get("Intake2");
        motorIntake = (DcMotorEx) hardwareMap.dcMotor.get("intake");

        servoTrigger = (Servo) hardwareMap.servo.get("trigga");
        voltageSensor = hardwareMap.get(VoltageSensor.class, "Control Hub");

        motorLaunch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLaunch.setDirection(DcMotorSimple.Direction.REVERSE);
        motorIntake.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    public class SetOutake implements Action {
        private double desiredOutakeSpeed;
        public SetOutake(double outakeSpeed) {
            super();
            desiredOutakeSpeed = outakeSpeed;
        }
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            voltage = voltageSensor.getVoltage();
            motorLaunch.setPower(desiredOutakeSpeed*13.5/voltage);
            return false;
        }
    }
    public SetOutake setOutake(double outakeSpeed) { return new SetOutake(outakeSpeed); }

    public class SetOutakeVelocity implements Action {
        private double desiredOutakeVelocity;
        public SetOutakeVelocity(double outakeVelocity) {
            super();
            desiredOutakeVelocity = outakeVelocity;
        }
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            motorLaunch.setPower(desiredOutakeVelocity);
            return false;
        }
    }
    public SetOutakeVelocity setOutakeVelocity(double outakeVelocity) { return new SetOutakeVelocity(outakeVelocity); }

    public class SetTrigger implements Action {
        // Use constructor parameter to set target position
        private double targetPosition;
        public SetTrigger(double Position) {
            super();
            targetPosition = Position;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            servoTrigger.setPosition(targetPosition);
            return false;
        }
    }
    public SetTrigger setTrigger(double Position) { return new SetTrigger(Position); }


    public class SetIntake implements Action {
        // Use constructor parameter to set target position
        private double desiredIntakeSpeed;
        public SetIntake(double intakeSpeed) {
            super();
            desiredIntakeSpeed = intakeSpeed;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            motorRamp1.setPower(desiredIntakeSpeed);
            motorRamp2.setPower(-desiredIntakeSpeed);
            motorIntake.setPower(Math.abs(desiredIntakeSpeed));
            return false;
        }
    }
    public SetIntake setIntake(double intakeSpeed) { return new SetIntake(intakeSpeed); }
}