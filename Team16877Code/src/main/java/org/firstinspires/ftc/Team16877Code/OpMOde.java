package org.firstinspires.ftc.Team16877Code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "WASD", group = "Linear Opmode")
public class OpMOde extends LinearOpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftBack;
    private DcMotor rightBack;

    private DcMotorSimple liftMotor;
    private DcMotorSimple parkingMotor;

    private Servo foundation1;
    private Servo foundation2;
    private Servo liftservo;

    private boolean rightBumperWasPressed;
    private boolean leftBumperWasPressed;
    private boolean backWasPressed;
    private boolean xWasPressed;
    private boolean bWasPressed;

    private boolean reverseControl = false;
    private double coef  = 1;

    private double leftFrontPower;
    private double rightFrontPower;
    private double leftBackPower;
    private double rightBackPower;
    private boolean rightBumper2WasPressed;
    private boolean leftBumper2WasPressed = false;


    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        leftFront = hardwareMap.get(DcMotor.class, "MotorFrontLeft");
        rightFront = hardwareMap.get(DcMotor.class, "MotorFrontRight");
        leftBack = hardwareMap.get(DcMotor.class, "MotorBackLeft");
        rightBack = hardwareMap.get(DcMotor.class, "MotorBackRight");

        parkingMotor = hardwareMap.get(DcMotorSimple.class, "motor5");
        liftMotor = hardwareMap.get(DcMotorSimple.class, "liftMotor");

        foundation1 = hardwareMap.get(Servo.class, "servoFoundation1");
        foundation2 = hardwareMap.get(Servo.class, "servoFoundation2");
        liftservo = hardwareMap.get(Servo.class, "liftServo");


        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftservo.setPosition(0);

        waitForStart();
        runtime.reset();


        foundation1.setPosition(0);
        foundation2.setPosition(1);

        while (opModeIsActive()) {
            leftFrontPower = (0);
            leftBackPower = (0);
            rightFrontPower = (0);
            rightBackPower = (0);

            if (gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_right && !gamepad1.dpad_left) { // Move forward
                leftFrontPower = (1);
                leftBackPower = (1);
                rightFrontPower = (-1);
                rightBackPower = (-1);
            } else if (!gamepad1.dpad_up && gamepad1.dpad_down && !gamepad1.dpad_right && !gamepad1.dpad_left) { // Move back
                leftFrontPower = (-1);
                leftBackPower = (-1);
                rightFrontPower = (1);
                rightBackPower = (1);
            } else if (!gamepad1.dpad_up && !gamepad1.dpad_down && gamepad1.dpad_right && !gamepad1.dpad_left) { // Move Right
                leftFrontPower = (1);
                leftBackPower = (-1);
                rightFrontPower = (1);
                rightBackPower = (-1);
            } else if (!gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_right && gamepad1.dpad_left) { // Move Left
                leftFrontPower = (-1);
                leftBackPower = (1);
                rightFrontPower = (-1);
                rightBackPower = (1);
            } else if (gamepad1.dpad_up && !gamepad1.dpad_down && gamepad1.dpad_right && !gamepad1.dpad_left) { // Move forwardRight
                leftFrontPower = (1);
                leftBackPower = (0);
                rightFrontPower = (0);
                rightBackPower = (-1);
            } else if (gamepad1.dpad_up && !gamepad1.dpad_down && !gamepad1.dpad_right && gamepad1.dpad_left) { // Move forwardLeft
                leftFrontPower = (0);
                leftBackPower = (1);
                rightFrontPower = (-1);
                rightBackPower = (0);
            } else if (!gamepad1.dpad_up && gamepad1.dpad_down && !gamepad1.dpad_right && gamepad1.dpad_left) { // Move downLeft
                leftFrontPower = (-1);
                leftBackPower = (0);
                rightFrontPower = (0);
                rightBackPower = (1);
            } else if (!gamepad1.dpad_up && gamepad1.dpad_down && gamepad1.dpad_right && !gamepad1.dpad_left) { // Move downRight
                leftFrontPower = (0);
                leftBackPower = (-1);
                rightFrontPower = (1);
                rightBackPower = (0);
            }
            if ((gamepad1.right_stick_y != 0) || (gamepad1.left_stick_y != 0)) { //Control by sticks
                if(!reverseControl){
                    leftFrontPower += (-gamepad1.left_stick_y);
                    leftBackPower += (-gamepad1.left_stick_y);
                    rightFrontPower += (gamepad1.right_stick_y);
                    rightBackPower += (gamepad1.right_stick_y);
                }
                else {
                    leftFrontPower += (-gamepad1.right_stick_y);
                    leftBackPower += (-gamepad1.right_stick_y);
                    rightFrontPower += (gamepad1.left_stick_y);
                    rightBackPower += (gamepad1.left_stick_y);
                }
            }

            if (gamepad1.back && !backWasPressed) {
                backWasPressed = true;
                reverseControl = !reverseControl;
            }
            else if (!gamepad1.back) {
                backWasPressed = false;
            }
            if (gamepad1.x && !xWasPressed) {
                xWasPressed = true;
                coef=Math.min(coef+0.5, 3);
            } else if (!gamepad1.x) {
                xWasPressed = false;
            }
            if (gamepad1.b && !bWasPressed) {
                bWasPressed = true;
                coef=Math.max(coef-0.5, 1);
            } else if (!gamepad1.b) {
                bWasPressed = false;
            }

           leftFrontPower =  leftFrontPower / coef;
           rightFrontPower = rightFrontPower / coef;
           leftBackPower = leftBackPower / coef;
           rightBackPower = rightBackPower / coef ;

            if (reverseControl) {
                leftFrontPower *=-1;
                rightFrontPower *=-1;
                leftBackPower *=-1;
                rightBackPower *=-1;
            }

            leftFront.setPower(leftFrontPower);
            rightFront.setPower(rightFrontPower);
            leftBack.setPower(leftBackPower);
            rightBack.setPower(rightBackPower);


                //lift servo control
            if (gamepad1.right_bumper && !rightBumperWasPressed) {
                if (liftservo.getPosition() != 0.8) {
                    rightBumperWasPressed = true;
                    liftservo.setPosition(0.8);
                }
                else {
                    rightBumperWasPressed = true;
                    liftservo.setPosition(0.3);
                }
            }
            else if (!gamepad1.right_bumper) {
                rightBumperWasPressed = false;
            }

            if (gamepad1.left_bumper && !leftBumperWasPressed) {
                if (foundation1.getPosition() < 0.05) {
                    leftBumperWasPressed = true;
                    foundation1.setPosition(1);
                    foundation2.setPosition(0);
                }
                else {
                    leftBumperWasPressed = true;
                    foundation1.setPosition(0);
                    foundation2.setPosition(1);
                }
            }
            else if (!gamepad1.left_bumper) {
                leftBumperWasPressed = false;
            }

            liftMotor.setPower(gamepad1.left_trigger-gamepad1.right_trigger);

            if (gamepad1.a){
                parkingMotor.setPower(1);
            }
            else if (gamepad1.y){
                parkingMotor.setPower(-1);
            }
            else{
                parkingMotor.setPower(0);
            }
        }
    }
}
