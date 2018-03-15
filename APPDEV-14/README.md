# Assignment 2: Mailing a pictue
Continue building on what you have made in APPDEV-12

## Reading materials
- App Data & Files
  - storage overview
  - Save files on Device storage
  - Save key-value data
  - *other topics not needed*
- Camera
  - Taking photos
- User interface and navigation
  - Toasts
  - *other topics not needed*
- Touch & input
  - input Events

## New app components
- New button with text 'Take picture' on the MainActivity
- New EditText on the MainActivity

## New app requirements
- Overall goal: include a picture in the email, and some preferences should be stored.
- User should insert his/her name in the textfield before continuing to email. [input events]
- The call and mail button are greyed out until you successfully took a picture and entered a name in the EditText [camera] 
- Picture should be stored to internal memory.  [app data and files] 
- Provide user with feedback. For instance when clicking on buttons which are disabled, or when taking a picture goes wrong (handling errors). [toasts]
- When user restarts the app, the previous entered name should be pre-inserted in the edittext [Save key-value data]
- Attach the photo to the email (from internal storage)

## Take in mind:
- The photo intent has a callback, the succes callback needs to enable the buttons. You can alter the button.enabled property for this. Only when picture is taken successfully!
- Use the 'Toast' infrastructure to give user feedback. 
- Be aware of storage issues and permissions! (read Permissions if needed: https://developer.android.com/guide/topics/permissions/index.html)

**Done when René receives an email**
