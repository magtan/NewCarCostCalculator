package com.example.magnust.vehiclecostproject;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by magnust on 25.07.2017.
 */

public class VehicleCost {

    private static final String TAG = "VehicleCost";

    //input
    private int yrsOwnCar = 3;
    private int carSize = 0; // 0 = small, 1 = medium, 2 = big
    private int cost = 300000;
    private boolean newCar = true;
    private int distancePrYear = 0;

    //default
    private float[] valueLossRate = {0.2f, 0.14f, 0.13f , 0.12f, 0.11f, 0.10f};
    private float interest = 0.04f;
    private float tires = 0;
    private float insurance = 0;
    private int[] wash = {600, 300, 0};
    private float service = 0;
    //private float[] service = {2000,2500,3000};
    //private float[] repair = {640,1633,2800};
    private int roadFee = 2820;
    private float fuelCostPrLitre = 14.00f;
    private float[] fuelConsumptionPrKm = {0.06f, 0.07f, 0.08f};
    private float[] defaultDistancePrYear = {8000f, 14000f, 20000f};
    private float taxFactor = 0.76f;
    private float[][] distFactor = {{12f, 21f, 30f},{10.32f, 18.06f, 25.8f},
            {9.098383838f,15.92222222f,22.7459596f},{8.229781818f, 14.40212121f, 20.57445455f},
            {7.638767677f, 13.36784625f, 19.09691919f},{7.271232323f, 12.72465925f, 18.17808081f}};
    private float[][] repFactor = {
            {0.08f,0.10f,0.12f},
            {0.08f,0.10f,0.12f},
            {0.12f,0.15f,0.18f},
            {0.12f,0.15f,0.18f},
            {0.16f,0.20f,0.24f},
            {0.20f,0.25f,0.30f},
            {0.24f,0.30f,0.36f},
            {0.32f,0.40f,0.48f},
            {0.48f,0.60f,0.72f},
            {0.56f,0.70f,0.84f},
            {0.68f,0.85f,1.02f},
            {0.68f,0.85f,1.02f},
            {0.68f,0.85f,1.02f},
            {0.64f,0.80f,0.96f}
    };

    //Variables
    private float valueLoss = 0;
    private float interestLoss = 0;

    //info
    private float totCost;
    private float avgCostPrKm;
    private float avgCostPrDay;
    private float marginalCostPr1Km;
    private float marginalCostFuel;


    public VehicleCost(int yrsOwnCar, int carSize, int cost , int dist, boolean newCar){
        this.yrsOwnCar = yrsOwnCar;
        this.carSize = carSize;
        this.cost = cost;
        this.distancePrYear = dist;
        this.newCar = newCar;
        calcCosts();
    }

    protected float getCurrentCarCost(){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        float hourlyCost = (avgCostPrDay/24)*hour;
        return hourlyCost;
    }


    private float getValueLoss(){return valueLoss;}
    private float getInterestLoss(){return interestLoss;}

    protected float getTotCost(){return totCost;}
    protected float getAvgCostPrKm(){return avgCostPrKm;}
    protected float getAvgCostPrDay(){return avgCostPrDay;}
    protected float getMarginalCostPr1Km(){return marginalCostPr1Km; }
    protected float getMarginalCostFuel() {return marginalCostFuel;}


    protected void calcCosts(){
        float fuelCost;
        float totInsurance;
        float totRoadFee;
        float totTireCost;
        float totWash;
        float totService;
        float totRepair;

        if(newCar){
            //newCarCalc
            calc();

            fuelCost = distancePrYear * fuelConsumptionPrKm[carSize] * fuelCostPrLitre * yrsOwnCar;
            totInsurance = calcInsurance();
            totRoadFee = roadFee * yrsOwnCar;
            totTireCost = calcTires(distancePrYear, yrsOwnCar);
            totWash = calcWash(distancePrYear, yrsOwnCar);
            totService = calcService(distancePrYear, yrsOwnCar);
            totRepair = calcRepCost(distancePrYear,yrsOwnCar);

            totCost = (fuelCost + valueLoss + interestLoss + totInsurance+ totRoadFee +
                    totTireCost + totWash + totService + totRepair);
            avgCostPrKm = totCost/(distancePrYear*yrsOwnCar);
            avgCostPrDay = totCost/(365*yrsOwnCar);
            calcMarginalCosts();

            String stringLog = "Total cost: " + totCost +"\nvalueLoss = " + valueLoss + "\ninterestCost =  "+ interestLoss +
                    "\nfuelCost = " + fuelCost +
                    "\ntotRoadFee = " + totRoadFee +
                    "\ntotInsurance = " + totInsurance +
                    "\ntotTireCost = " + totTireCost +
                    "\ntotWash = " + totWash +
                    "\ntotService = " + totService +
                    "\ntotRepair = " + totRepair;
            Log.i(TAG, stringLog);

        }else{
            //oldCarCalc

        }

    }

    private float calcService(int dist, int yr){
        float totService;

        if(service == 0){
            switch (carSize){
                case 0:
                    totService = (0.25f*dist)*yr;
                    return totService;
                case 1:
                    totService = (50f+0.175f*dist)*yr;
                    return totService;
                case 2:
                    totService = (0.15f*dist)*yr;
                    return totService;
                default:
                    return 0;
            }
        }else{
            return service * yrsOwnCar;
        }
    }

    private float calcWash(int dist, int yr){
        float totWash;

        totWash = (wash[carSize] + (4f/80f)*dist)*yr;

        return totWash;
    }

    private float calcInsurance(){
        float totInsurance;
        if(insurance == 0){
            switch (carSize){
                case 0:
                    totInsurance = (4000f+0.125f*distancePrYear)*yrsOwnCar;
                    return totInsurance;
                case 1:
                    totInsurance = (4900f+0.150f*distancePrYear)*yrsOwnCar;
                    return totInsurance;
                case 2:
                    totInsurance = (5500+0.175f*distancePrYear)*yrsOwnCar;
                    return totInsurance;
                default:
                    return 0;
            }
        }else{
            return insurance*yrsOwnCar;
        }
    }

    private float calcTires(int dist, int yr){
        float totTires;
        if(tires == 0){
            switch (carSize){
                case 0:
                    totTires = (610f + 0.08f*dist)*yr;
                    return totTires;
                case 1:
                    totTires = (100f+0.1f*dist)*yr;
                    return totTires;
                case 2:
                    totTires = (-650f + 0.12f*dist)*yr;
                    return totTires;
                default:
                    return 0;
            }
        }else{
            return tires*yrsOwnCar;
        }
    }

    private float calcRepCost(int dist, int yr){
        float repCost = 0;
        float accumDist = dist;

        for (int i=0; i<yr;i++){
            if(accumDist <= 20000f){
                repCost += repFactor[0][carSize]*dist;
            }else if(accumDist <= 30000f){
                repCost += repFactor[1][carSize]*dist;
            }else if(accumDist <= 40000f){
                repCost += repFactor[2][carSize]*dist;
            }else if(accumDist <= 50000f){
                repCost += repFactor[3][carSize]*dist;
            }else if(accumDist <= 60000f){
                repCost += repFactor[4][carSize]*dist;
            }else if(accumDist <= 70000f){
                repCost += repFactor[5][carSize]*dist;
            }else if(accumDist <= 80000f){
                repCost += repFactor[6][carSize]*dist;
            }else if(accumDist <= 90000f){
                repCost += repFactor[7][carSize]*dist;
            }else if(accumDist <= 100000f){
                repCost += repFactor[8][carSize]*dist;
            }else if(accumDist <= 110000f){
                repCost += repFactor[9][carSize]*dist;
            }else if(accumDist <= 120000f){
                repCost += repFactor[10][carSize]*dist;
            }else if(accumDist <= 130000f){
                repCost += repFactor[11][carSize]*dist;
            }else if(accumDist <= 140000f){
                repCost += repFactor[12][carSize]*dist;
            }else{
                repCost += repFactor[13][carSize]*dist;
            }
            String debug = "Distance acc.: " + accumDist + "\nrepCost: "+ repCost;
            Log.i(TAG,debug);
            accumDist += distancePrYear;

        }
        return repCost;
    }

    private float getServicePrKm(){
        switch (carSize){
            case 0:
                return 0.25f;
            case 1:
                return 0.175f;
            case 2:
                return 0.15f;
            default:
                return 0.25f;
        }
    }

    private float getTiresPrKm(){
        switch (carSize){
            case 0:
                return 0.08f;
            case 1:
                return 0.1f;
            case 2:
                return 0.12f;
            default:
                return 0.08f;
        }
    }
    private float getInsurancePrKm(){

        switch (carSize){
            case 0:
                return 0.125f;
            case 1:
                return 0.150f;
            case 2:
                return 0.175f;
            default:
                return 0.125f;
        }
    }

    private float getWashPrKm(){

        return (4f/80f);
    }

    private void calcMarginalCosts() {
        float marginalCostFuel = fuelConsumptionPrKm[carSize] * fuelCostPrLitre;
        float marginalCostPr1Km = calcRepCost(1,1) + getServicePrKm() + getWashPrKm() +
                getTiresPrKm() + getInsurancePrKm() +
                marginalCostFuel;

        switch (carSize){
            case 0:
                marginalCostPr1Km += 0.01f*(cost/20000f);
                break;
            case 1:
                marginalCostPr1Km += 0.01f*(cost/20000f);
                break;
            case 2:
                marginalCostPr1Km += 0.01f*(cost/20000f);
                break;
            default:
                marginalCostPr1Km += 0.01f*(cost/20000f);
                break;
        }
        this.marginalCostPr1Km = marginalCostPr1Km;
        this.marginalCostFuel = marginalCostFuel;
    }


    private void calc(){

        // Variables related to normal value loss (no km correction)
        float normalValueLoss = 0;
        float totNormalValueLoss = 0;

        float normalCurrentValue = cost;

        // Variables related to km correction
        float correctedLossDiff =0;
        float totCorrectedLossDiff = 0;

        float correctedCurrentValue = cost;

        // Losses related to interest
        float totInterestLoss = 0;
        float interestLoss = 0;

        if(distancePrYear == 0){
            distancePrYear = (int)defaultDistancePrYear[carSize];
        }

        for(int i = 0; i < yrsOwnCar; i++){

            correctedLossDiff = (getDistanceFactor(i)*((distancePrYear/defaultDistancePrYear[carSize])-1f)*100f);
            totCorrectedLossDiff += correctedLossDiff;

            String distfac= "factor: " + getDistanceFactor(i) + "\ndiff: " + correctedLossDiff;
            Log.i(TAG, distfac);
            if(i>=5) {

                normalValueLoss = normalCurrentValue * valueLossRate[5];
                totNormalValueLoss += normalValueLoss;

            }else{
                normalValueLoss = normalCurrentValue * valueLossRate[i];
                totNormalValueLoss += normalValueLoss;
            }


            interestLoss = 0.76f*(correctedCurrentValue)*interest*
                    (1f-(((normalValueLoss+correctedLossDiff)/(correctedCurrentValue))/2f));
            totInterestLoss += interestLoss;

            normalCurrentValue = cost - totNormalValueLoss;
            correctedCurrentValue = cost - totNormalValueLoss - totCorrectedLossDiff;

            //----------------------------------------------------------------------
            //Only for debugging
            String log = ""+totNormalValueLoss;
            String log2 = ""+normalCurrentValue;
            String log3 = ""+totCorrectedLossDiff;
            Log.i(TAG,"totNormalValueLoss: "+log + "\nnormalCurrentValue: " + log2 +
            "\ntotCorrectedLossDiff:" + log3);
            //-----------------------------------------------------------------------
        }

        valueLoss = totNormalValueLoss + totCorrectedLossDiff;
        this.interestLoss = totInterestLoss;
    }

    private float getDistanceFactor(int yr){
        return distFactor[yr][carSize];
    }
}
