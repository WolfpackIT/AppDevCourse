# After completing APPDEV-11 do the following:

First read: introduction to activities and activity life-cycle (APPDEV-11)

## Make an app with the following components
Main activity with the following GUI elements: one button called 'email WP office 'and one button called 'Call WP office' as label.
EmailWPActivity; empty activity
CallWPActivity; empty activity
One single fragment called 'ClickToContinueFragment' with the following GUI element: one button with 'placeholder' as label.

## The app has to be able to do the following things: (requirements)
When you click on 'email WP office' a new activity is started with a button saying 'mail now' When you click the button You will see the default android 'intent chooser' which asks you which email client to use. When you select, a prefilled email is openend with title: 'HELLO WORLD' and content 'I did it, greetings [insertnamehere] to rene.le.clercq@wolfpackit.nl'
When you click on 'call WP office' a new activity is started with a button saying 'call now'. After which you again use 'intent filter' to start the phone dialing options, with predialed 040-782 0814

## Take in mind:
Please refer to the topics 'parcelables and bundles', ' fragments' and 'interact with other apps' for help. 
Since the fragment you use is generic (same fragment for both actions) you need to have some sort of data passed through' to the fragment about which was the calller (email or call button). You are not allowed to hardcode this. Please use 'parcables and bundles' for this.
Correctly use onCreate, onViewCreated, onStart etc. (see 'activity lifecycle')
Best to run on your phone to actually send the email or place the call. Emulator is not able to do that.

**Done when I receive a phonecall and email.**
