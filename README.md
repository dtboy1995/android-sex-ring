# android-sex-ring
:ring:  a android ring view that can sweep angle [![Build Status](https://travis-ci.org/dtboy1995/android-sex-ring.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-ring)

# install
- add to your project gradle file

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- add to your module gradle file

```gradle
implementation 'com.github.dtboy1995:android-sex-ring:0.1.0'
```

# usage
- XML

```xml
<org.ithot.android.view.Ring
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_marginLeft="20dp"
        android:layout_marginTop="20dp"
        app:animateDuration="2000"
        app:animateType="decelerate"
        app:backgroundColor="#DADADE"
        app:foregroundColor="#00BCD3"
        app:shadowColor="#33333333"
        app:shadowEnable="true"
        app:shadowRadius="10"
        app:startAngle="120"
        app:strokeCap="round"
        app:strokeWidth="8dp"
        app:sweepAngle="300" />
```
- Code

```java
Ring ring = (Ring)findViewById(R.id.ring);
ring.setCallback(new AVCallback() {
      @Override
      public void step(int progress) {
        // progress 0~100
        textView.setText(progress + "");
      }
});

```
