

##########################################大白兔混淆規則####################################


# 指定代码的压缩级别 0 - 7(指定代码进行迭代优化的次数，在Android里面默认是5，这条指令也只有在可以优化时起作用。)
-optimizationpasses 5
# 混淆时不会产生形形色色的类名(混淆时不使用大小写混合类名)
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库类(不跳过library中的非public的类)
-dontskipnonpubliclibraryclasses
# 指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
 # 不进行预校验,Android不需要,可加快混淆速度。
-dontpreverify
 # 混淆时记录日志(打印混淆的详细信息)
 # 这句话能够使我们的项目混淆后产生映射文件
 # 包含有类名->混淆后类名的映射关系
-verbose
-printmapping priguardMapping.txt
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*
#表示不混淆Parcelable实现类中的CREATOR字段，
#毫无疑问，CREATOR字段是绝对不能改变的，包括大小写都不能变，不然整个Parcelable工作机制都会失败。
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 这指定了继承Serizalizable的类的如下成员不被移除混淆
-keepnames class * implements java.io.Serializable
-keepattributes Signature
-keep class **.R$* {*;}
-ignorewarnings
#不混淆资源类下static的
-keepclassmembers class **.R$* {
    public static <fields>;
}
# 保持native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用，
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.sxtx.user.** { *; }
###############greendao的混淆编译开始###############
-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

###############autosize###############
-keep class me.jessyan.autosize.** { *; }
-keep interface me.jessyan.autosize.** { *; }

###############Support###############
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**
#一定要加上如下两行代码，否则XmlResourceParser会报错
-keep class org.xmlpull.v1.** { *;}
-dontwarn org.xmlpull.v1.**

###############eventBus###############
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

###############bga-banner###############
-keep class cn.bingoogolapple.bgabanner.BGAViewPager { *; }

###############stetho###############
-keep class com.facebook.stetho.** { *; }
-dontwarn com.facebook.stetho.**

###############BaseRecyclerViewAdapterHelper###############
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
-keepattributes InnerClasses

###############Glide###############
 -keep public class * implements com.like.utilslib.image.config.MyAppGlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
 }

###############banner###############
 -keep class com.youth.banner.** {
     *;
  }

###############RoundedImageView###############
-dontwarn com.squareup.okhttp.**
 -dontwarn com.squareup.picasso.Transformation

 ###############android-gif-drawable###############
 -keep public class pl.droidsonroids.gif.GifIOException{<init>(int, java.lang.String);}
 -dontwarn androidx.annotation.**

  ###############protobuf###############
 -keep class com.lyh.protocol.** { *; }

  ###############GSYVideoPlayer###############
 -keep class com.shuyu.gsyvideoplayer.video.** { *; }
 -dontwarn com.shuyu.gsyvideoplayer.video.**
 -keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
 -dontwarn com.shuyu.gsyvideoplayer.video.base.**
 -keep class com.shuyu.gsyvideoplayer.utils.** { *; }
 -dontwarn com.shuyu.gsyvideoplayer.utils.**
 -keep class tv.danmaku.ijk.** { *; }
 -dontwarn tv.danmaku.ijk.**

 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 ###############阿里云###############
 -keep class com.alibaba.sdk.android.oss.** { *; }
 -dontwarn okio.**
 -dontwarn org.apache.commons.codec.binary.**

 ###############SVGA###############
 -keep class com.squareup.wire.** { *; }
 -keep class com.opensource.svgaplayer.proto.** { *; }


