# ColorPickerDialogStandAlone

#### This Color Picker is also available as a library/module, for that refer [ColorPickerDialog](https://github.com/RealDev05/ColorPickerDialog.git)

- This standalone class is written in java
- The is an extension of Alert Dialog Builder
- Most components are declared public so that they can be accessed from anywhere, thereby increasing versatility.
- Even though, messing with some variables and values can result in unecpected results
- This class was made for android ***SDK 21*** and above
- Can provide the color in ***integer*** and ***hex string*** form (eg : 0,1 and #FFFFFFFF, #FF000000)
- Hex string returned is in ***#AARRGGBB*** form 

## Setup
- Copy the java file, that is, ***ColorPickerDialogBuilder.java*** to your project
- The class is declared with package name 
- You are all set
- Contact me if there is any doubts/problems
## Usage
```
ColorPickerDialogBuilder colorPicker = new ColorPickerDialogBuilder(this);
colorPicker.show(new ColorPickerDialogBuilder.ColorPickedListener() {
    @Override
    
    public void OnColorPicked(int color, String hex) {
            
            
    }
    
});
```
