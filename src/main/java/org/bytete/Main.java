package org.bytete;

import org.bytete.Callbacks.ParameterReturnTypeCallback;
import org.bytete.Callbacks.ParameterVoidCallback;
import org.bytete.Callbacks.ReturnTypeCallback;

public class Main{
    public static void main(String args[]) throws InterruptedException {


        Async async = new Async(() -> {
            int money = 10;
            return money;
        }).then((ParameterReturnTypeCallback<Integer, Integer>) (Integer data) -> {
            System.out.println("do next step");
            return data + 20;
        }).then((ParameterReturnTypeCallback<Integer, String>) (Integer data) -> {
            System.out.println(data);
            return data + " lastValue";
        }).then((ParameterVoidCallback<String>) (String data) -> {
            System.out.println(data);
        }).then(() -> {
            System.out.println("do next step");
        }).catchError((e) -> {
            System.out.println(e);
        }).handleFinally(() -> {
            System.out.println("clean codes");
        });

        async.async();
 
    }
}