# JavaAsync
Write your Java code in async way
### Intro
This library was created for javascript developer who love with async/await and don't want to handle thread in Java program.

#### How it work?
Basically it use Java Thread to stimulate async behaviour. **async()** function sprawn a new thread and execute the function as queue. **await()** function execute function as queue without sprawning a new Java Thread.



##### Created By
@JianShangQuan


### Usage


``` java
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

async.async(); // this will execute async way
async.await(); // this will execute syncronized way, and will block program

```
