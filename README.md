# in-app-manager 
[![](https://jitpack.io/v/devAhmadHamdy/in-app-manager.svg)](https://jitpack.io/#devAhmadHamdy/in-app-manager)

Android Library to simplify some android in app functions ( In app billing, in app update, in app review)

# Setup

Add this in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

```
Then add this to your dependancies 

```	      
implementation 'com.github.devAhmadHamdy:in-app-manager:0.0.1'
```

# 1- In app app review

Pass your context and your parameter function to be called when review is done
# Note:
You will not be able to submit your rating in testing case so submit button will be disabled 

```	      
  InAppReview().startReview(
            this
        ) {
            Log.d("in app review", "success")
        }
```

# 2- In app app update

Pass your context, Update type and request code
# Note: 
0 for flexible update and 1 for immediate 

```	      
 InAppUpdate().checkForUpdate(this, UPDATE_TYPE_FLEXIBLE, UPDATE_REQUEST_CODE)

```

# 3- In app app billing

Pass your context, your package id you need to purchase and parameter function to be called when product is purchased

```	      
 InAppBilling().startPurchase(this, "YOUR_PACKAGE_ID") {
            Log.e(
                "Billing result",
                "Done"
            )
        }

```

# Take care of the following

- In case of In app update you will need to test a version uploaded to internal app sharing, first enable it from your console and from your google play in your device, for details check https://support.google.com/googleplay/android-developer/answer/9844679?hl=en#zippy=%2Cupload-and-share-apps-for-testing
- In case of in app billing make sure you enabled  # Merchant serivce, you have a valid product and your account added to testers 





