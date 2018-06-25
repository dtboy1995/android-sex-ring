# android-sex-ring [![Build Status](https://travis-ci.org/dtboy1995/android-sex-ring.svg?branch=master)](https://travis-ci.org/dtboy1995/android-sex-ring)
:jack_o_lantern:  a android ring view that can sweep angle

# install
- add to your project gradle file

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- add to your module gradle file

```gradle
implementation 'com.github.dtboy1995:android-sex-ring:0.2.0'
```

# usage
- XML

```xml
<org.ithot.android.view.RingView
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
        app:touchable="true"
        app:startAngle="120"
        app:strokeCap="round"
        app:strokeWidth="8dp"
        app:sweepAngle="300" />
```
- Code

```java
RingView rv = (RingView)findViewById(R.id.ring_view);
// set progress - true is animated
rv.go(20, true);
// set progress callback
rv.setCallback(new AVCallback() {
    @Override
    public void step(int progress) {
      // progress 0~100
      tv.setText(progress + "");
    }
});
// set range maped progress callback [-20, 30] map to => [0, 100]
rv.setCallback(new AVRangeMapCallback(-20, 30) {
    @Override
    public void step(int progress) {
      // progress -20~30
      tv.setText(progress + "");
    }
});
```

# attrs

name | type | desc
:-: | :-: | :-:
strokeWidth | reference,dimension | dp, @dimen/
backgroundColor | reference,color | #ffffff, @color/
foregroundColor | reference,color | #ffffff, @color/
startAngle | integer | 0~360
animateDuration | integer | number
sweepAngle | integer | 0~360
shadowRadius | integer | number
onStep | string,reference | function
shadowEnable | boolean | true,false
touchable | boolean | true,false
shadowColor | reference,color | #ffffff, @color/
animateType | enum | linear,accelerate,decelerate
strokeCap | enum | round,butt,square
