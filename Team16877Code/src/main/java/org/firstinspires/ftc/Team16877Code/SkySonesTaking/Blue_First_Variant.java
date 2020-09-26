package org.firstinspires.ftc.Team16877Code.SkySonesTaking;

//import

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.Team16877Code.AutonautsAPI;

import static java.lang.Math.abs;

@Autonomous(name = "BYXX")
public class Blue_First_Variant extends AutonautsAPI {

    @Override
    public void runOpMode() {

        INIT();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitStart();

        double power = 0.8;

        liftServo(false);
        runForward(0.59,power);
        runRight(1.15,power);
        liftServo(false);
        runForward(0.18,power);
        liftServo(true);
        runBack(0.19,power);
        runLeft(1.17,power);
        liftMotor(1500,0.5);
        runLeft(0.85, power);
        runForward(0.25,power);
        liftServo(false);
        setFoundation(true);
        runBack(0.8,power);
        turnTo(90,power);
        runForward(0.15,power);
        setFoundation(false);
        runRight(0.4,power);
        runBack(0.1,power);
        liftMotor(1500,-0.5);
        runBack(0.8,power);
    }
}
