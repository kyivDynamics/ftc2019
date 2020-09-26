package org.firstinspires.ftc.Team16877Code.SkySonesTaking;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.Team16877Code.AutonautsAPI;

import static java.lang.Math.abs;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="RXYX")
public class Red_Second_Variant extends AutonautsAPI {
    @Override
    public void runOpMode() {
        INIT();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitStart();

        double power = 0.8;

        liftServo(false);
        runForward(0.57,power);
        runLeft(1.37,power);
        liftServo(false);
        runForward(0.23,power);
        liftServo(true);
        runBack(0.23,power);
        runRight(1.39,power);
        liftMotor(1500,0.5);
        runRight(0.85, power);
        runForward(0.25,power);
        liftServo(false);
        setFoundation(true);
        runBack(0.8,power);
        turnTo(-90,power*0.8);
        runForward(0.15,power);
        setFoundation(false);
        runLeft(0.4,power);
        runBack(0.9,power);
        liftMotor(1500,-0.5);
    }
}
