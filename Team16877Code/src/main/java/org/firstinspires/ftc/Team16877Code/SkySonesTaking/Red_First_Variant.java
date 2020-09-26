package org.firstinspires.ftc.Team16877Code.SkySonesTaking;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.Team16877Code.AutonautsAPI;

import static java.lang.Math.abs;

//import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="RYXX")
public class Red_First_Variant extends AutonautsAPI {

    @Override
    public void runOpMode() {

        INIT();
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitStart();

        double power = 0.8;

        liftServo(false);
        runForward(0.58,power);
        runLeft(1.125,power);
        liftServo(false);
        turnTo(0,0.6);
        runForward(0.18,power);
        liftServo(true);
        runBack(0.19,power);
        runRight(1.19,power);
        turnTo(0,0.6);
        liftMotor(1500,0.6);
        runRight(0.65, power);
        runForward(0.40,power);
        liftServo(false);
        setFoundation(true);
        runBack(0.80,power);
        turnTo(-90,0.56);
        runForward(0.17,power);
        setFoundation(false);
        runLeft(0.4,power);
        runBack(0.1,power);
        liftMotor(1500,-0.5);
        runBack(0.8,power);
    }
}
