package info.mrconst.qlient;

import android.app.Activity;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import info.mrconst.qlient.inputmethod.KeyboardView;

public class QuestKeyboard {

    private KeyboardView mKeyboardView;
    private Activity mCtx;
    private int mCurrentLayout;
    private int[] mKbdLayouts;

    private int mCurrentFont = 0;
    private static String[] sKbdFonts = {FontManager.DEFAULT, FontManager.BRAILLE, FontManager.SEMAPHORE};

    public final static int CodeDelete   = Keyboard.KEYCODE_DELETE;
    public final static int CodeCancel   = Keyboard.KEYCODE_CANCEL;
    public final static int CodePrev     = 55000;
    public final static int CodeAllLeft  = 55001;
    public final static int CodeLeft     = 55002;
    public final static int CodeRight    = 55003;
    public final static int CodeAllRight = 55004;
    public final static int CodeNext     = 55005;
    public final static int CodeClear    = 55006;
    public final static int CodeChange   = Keyboard.KEYCODE_MODE_CHANGE;
    static final int KEYCODE_LANGUAGE_SWITCH = -101;

    public QuestKeyboard(Activity host, int viewid, int[] kbdLayouts) {
        mCurrentLayout = 0;
        mKbdLayouts = kbdLayouts;
        mCtx = host;
        mKeyboardView = (KeyboardView)host.findViewById(viewid);
        mKeyboardView.setKeyboard( new Keyboard(host, mKbdLayouts[mCurrentLayout]) );
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener() );

        host.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setKeyboardTypeface(Typeface typeface) {
        mKeyboardView.setCurrentTypeface(typeface);
    }

    public int getCurrentLayout() {
        return mKbdLayouts[mCurrentLayout];
    }

    public void hideCustomKeyboard() {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }

    public void showCustomKeyboard( View v ) {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);

        if( v!=null ) ((InputMethodManager)mCtx.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    public void registerEditText(EditText edittext) {
        // Make the custom keyboard appear
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if( hasFocus ) showCustomKeyboard(v); else hideCustomKeyboard();
            }
        });
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showCustomKeyboard(v);
            }
        });
        // Disable standard keyboard hard way
        edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                return true; // Consume touch event
            }
        });
        // Disable spell check (hex strings look like words to Android)
        edittext.setInputType( edittext.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS );
    }

    class OnKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // Get the EditText and its Editable
            View focusCurrent = mCtx.getWindow().getCurrentFocus();
            if( focusCurrent==null || !(focusCurrent instanceof EditText) ) return;
            EditText edittext = (EditText) focusCurrent;
            Editable editable = edittext.getText();
            if (editable == null)
                return;
            int start = edittext.getSelectionStart();
            // Handle key
            switch (primaryCode) {
                case CodeCancel:
                    hideCustomKeyboard();
                    break;
                case CodeDelete:
                    if (start > 0) editable.delete(start - 1, start);
                    break;
                case CodeClear:
                    editable.clear();
                    break;
                case CodeLeft:
                    if (start > 0) edittext.setSelection(start - 1);
                    break;
                case CodeRight:
                    if (start < edittext.length()) edittext.setSelection(start + 1);
                    break;
                case CodeAllLeft:
                    edittext.setSelection(0);
                    break;
                case CodeAllRight:
                    edittext.setSelection(edittext.length());
                    break;
                case CodePrev: {
                    View focusNew = edittext.focusSearch(View.FOCUS_BACKWARD);
                    if (focusNew != null) focusNew.requestFocus();
                    break;
                }
                case CodeNext: {
                    View focusNew = edittext.focusSearch(View.FOCUS_FORWARD);
                    if (focusNew != null) focusNew.requestFocus();
                    break;
                }
                case CodeChange:
                    int count = mKbdLayouts.length;
                    mCurrentLayout = (mCurrentLayout + 1) % count;
                    mKeyboardView.setKeyboard(new Keyboard(mCtx, mKbdLayouts[mCurrentLayout]));
                    break;
                case KEYCODE_LANGUAGE_SWITCH:
                    mCurrentFont = (mCurrentFont + 1) % sKbdFonts.length;
                    mKeyboardView.setCurrentTypeface(FontManager.getTypeface(sKbdFonts[mCurrentFont]));
                    break;
                default: // Insert character
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    }
}
