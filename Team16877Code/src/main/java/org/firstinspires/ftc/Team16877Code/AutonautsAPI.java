package org.firstinspires.ftc.Team16877Code;

import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.getExponent;

public class AutonautsAPI extends LinearOpMode {

    // imu init start
    public BNO055IMU imu = null;
    public Orientation lastAngles = new Orientation();
    public double globalAngle;

    //imu init end

    static final double MOTOR_TO_REV_TICKS = 537.6;
    static final double DIAMETR_WHEEL = 3.1415 * 0.075;
    static final double DIAMETR_WHEEL_SIDE = 0.22;
    static final ElapsedTime runtime = new ElapsedTime();
    static DcMotor leftFront;
    static DcMotor rightFront;
    static DcMotor leftBack;
    static DcMotor rightBack;
    static DcMotorSimple liftMotor;
    static Servo liftservo;
    static Servo foundation1;
    static Servo foundation2;
    @Override
    public void runOpMode()
    {

    }

    public void waitStart(){
        while (!opModeIsActive());
    }
    public void INIT(){

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode               = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class,"imu");

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".


        telemetry.addData("1",true);
        telemetry.update();
        imu.initialize(parameters);
        sleep(1000);
        telemetry.addData("Init successfully"," crush");
        telemetry.update();

        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        leftFront  = hardwareMap.get(DcMotor.class, "MotorFrontLeft");
        rightFront = hardwareMap.get(DcMotor.class, "MotorFrontRight");
        leftBack   = hardwareMap.get(DcMotor.class, "MotorBackLeft");
        rightBack  = hardwareMap.get(DcMotor.class, "MotorBackRight");

        liftMotor = hardwareMap.get(DcMotorSimple.class, "liftMotor");

        liftservo = hardwareMap.get(Servo.class, "liftServo");
        foundation1 = hardwareMap.get(Servo.class, "servoFoundation1");
        foundation2 = hardwareMap.get(Servo.class, "servoFoundation2");

        leftFront.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftservo.setPosition(0.1);

        foundation1.setPosition(0);
        foundation2.setPosition(1);

        waitForStart();
        runtime.reset();
    }

    public void runForward(double meters,double power){
        leftFront.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));
        leftBack.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));
        rightFront.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));
        rightBack.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));

        leftFront.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(-power);
        leftBack.setPower(-power);
        rightFront.setPower(power);
        rightBack.setPower(power);

        while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()){
//            telemetry.addData("WAITING","GOING");
//            telemetry.addData("LF",  leftFront.getCurrentPosition());
//            telemetry.addData("RF",  rightFront.getCurrentPosition());
//            telemetry.addData("LB",  leftBack.getCurrentPosition());
//            telemetry.addData("RB",  rightBack.getCurrentPosition());
//            telemetry.update();
        }
        rightBack.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        leftFront.setPower(0);


    }
    public void runRight(double meters,double power){
        leftFront.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);



        leftFront.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));
        leftBack.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));
        rightFront.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));
        rightBack.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));

        leftFront.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(-power);
        leftBack.setPower(power);
        rightFront.setPower(-power);
        rightBack.setPower(power);

        while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()){
//            telemetry.addData("WAITING","GOING");
//            telemetry.addData("LF",  leftFront.getCurrentPosition());
//            telemetry.addData("RF",  rightFront.getCurrentPosition());
//            telemetry.addData("LB",  leftBack.getCurrentPosition());
//            telemetry.addData("RB",  rightBack.getCurrentPosition());
//            telemetry.update();
        }
        rightBack.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        leftFront.setPower(0);


    }
    public void runLeft(double meters,double power){
        leftFront.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);



        leftFront.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));
        leftBack.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));
        rightFront.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));
        rightBack.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL_SIDE)));

        leftFront.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(-power);
        rightFront.setPower(power);
        rightBack.setPower(-power);

        while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy()){
//            telemetry.addData("WAITING","GOING");
//            telemetry.addData("LF",  leftFront.getCurrentPosition());
//            telemetry.addData("RF",  rightFront.getCurrentPosition());
//            telemetry.addData("LB",  leftBack.getCurrentPosition());
//            telemetry.addData("RB",  rightBack.getCurrentPosition());
//            telemetry.update();
        }
        rightBack.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        leftFront.setPower(0);


    }
    public void runBack(double meters,double power){
        leftFront.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode   (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode (DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode  (DcMotor.RunMode.STOP_AND_RESET_ENCODER);



        leftFront.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));
        leftBack.setTargetPosition((int)(-MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));
        rightFront.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));
        rightBack.setTargetPosition((int)(MOTOR_TO_REV_TICKS*(meters/DIAMETR_WHEEL)));

        leftFront.setMode  (DcMotor.RunMode.RUN_TO_POSITION);
        leftBack.setMode   (DcMotor.RunMode.RUN_TO_POSITION);
        rightFront.setMode (DcMotor.RunMode.RUN_TO_POSITION);
        rightBack.setMode  (DcMotor.RunMode.RUN_TO_POSITION);

        leftFront.setPower(power);
        leftBack.setPower(power);
        rightFront.setPower(-power);
        rightBack.setPower(-power);

        while (leftFront.isBusy() && leftBack.isBusy() && rightFront.isBusy() && rightBack.isBusy());
        rightBack.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        leftFront.setPower(0);


    }
    public void liftServo(boolean i){
        if(i) liftservo.setPosition(0.8);
        else liftservo.setPosition(0.5);
        sleep(150);
    }
    public void setFoundation(boolean i){
        if(!i){
            foundation1.setPosition(0);
            foundation2.setPosition(1);
        }else{
            foundation1.setPosition(1);
            foundation2.setPosition(0);
        }
        sleep(700);
    }
    public void turnTo(double wantAngle, double power) {
        leftFront.setMode  (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode   (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode (DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode  (DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (Math.abs(Math.abs(wantAngle) - Math.abs(getAngle())) > 2)
        {
            telemetry.addData("Want Angle", wantAngle);
            telemetry.addData("Angle", getAngle());
            telemetry.update();

//            leftFront.setPower(((Math.abs(wantAngle) - Math.abs(getAngle())) / 45)*power + 0.05);
//            rightFront.setPower(((Math.abs(wantAngle) - Math.abs(getAngle())) / 45)*power + 0.05);

            if (wantAngle < getAngle()) {
                if (Math.abs(Math.abs(wantAngle) - Math.abs(getAngle())) > 20){
                    leftFront.setPower(power);
                    rightFront.setPower(power);
                    leftBack.setPower(power);
                    rightBack.setPower(power);
                } else {
                    leftFront.setPower(power/2.5);
                    rightFront.setPower(power/2.5);
                    leftBack.setPower(power/2.5);
                    rightBack.setPower(power/2.5);
                }
            } else {
                if (Math.abs(Math.abs(wantAngle) - Math.abs(getAngle())) > 20){
                    leftFront.setPower(-power);
                    rightFront.setPower(-power);
                    leftBack.setPower(-power);
                    rightBack.setPower(-power);
                } else {
                    leftFront.setPower(-power/2.5);
                    rightFront.setPower(-power/2.5);
                    leftBack.setPower(-power/2.5);
                    rightBack.setPower(-power/2.5);
                }
            }
        }

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);
    }
    public void resetAngle() {
        lastAngles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        globalAngle = 0;
    }
    public double getAngle() {

        Orientation angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastAngles = angles;

        return globalAngle;
    }
    public void liftMotor(long milisecond, double power){
        liftMotor.setPower(power);
        sleep(milisecond);
        liftMotor.setPower(0);
    }

}
