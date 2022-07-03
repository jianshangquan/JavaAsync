package org.bytete;

import org.bytete.Callbacks.ParameterReturnTypeCallback;
import org.bytete.Callbacks.ParameterVoidCallback;

public class Main{
    public static void main(String args[]) throws InterruptedException {


        Async async = new Async(() -> {
            int money = 10;
            return money;
        }).then(() -> {
            System.out.println("do next step");
        }).then((ParameterReturnTypeCallback<Integer, String>) (Integer data) -> {
            System.out.println(data);
            return "lastValue";
        }).then((ParameterVoidCallback<String>) (String data) -> {
            System.out.println(data);
        }).catchError((e) -> {
            System.out.println(e);
        });


        async.async();
 
    }
}