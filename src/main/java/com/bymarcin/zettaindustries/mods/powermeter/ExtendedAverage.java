package com.bymarcin.zettaindustries.mods.powermeter;

 class ExtendedAverage {
    
    private double[] numbers;
    private int current = 0;
    
    private double average;
    private boolean dirty = true;

    public ExtendedAverage(int size){
        numbers = new double[size];
    }
    
    public void add(double n){
        numbers[current] = n;
        current++;
        current %= numbers.length;
        dirty = true;
    }
    
    public double get() {
        if(!dirty)
            return average;
        
        average = 0;
        for(double d : numbers){
            average += d;
        }
        average/= numbers.length;
        dirty = false;
        return average;
    }
    
}