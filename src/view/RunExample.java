package view;

import controller.Controller;
import exceptions.MyException;

import java.io.IOException;
import java.util.Scanner;

public class RunExample extends Command {
    private Controller ctr;
    public RunExample(String key, String desc,Controller ctr){
        super(key, desc);
        this.ctr=ctr;
    }

    @Override
    public void execute() {
        try{
            ctr.allStep();
        } catch (Exception e)  {
            System.out.println("Execution error: " + e.getMessage());
            System.out.println();
        }
    }
}
