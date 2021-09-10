package com.michaelam.KeyboardListener;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;


@DesignerComponent(
        version = KeyboardListener.VERSION,
        description =
                "An extension that listens to keyboardState events.",
        category = ComponentCategory.EXTENSION, nonVisible = true,
        iconName = "https://i.ibb.co/6sRzbvh/kicon.png")
@SimpleObject(external = true)
public class KeyboardListener extends AndroidNonvisibleComponent {
    public static final int VERSION = 1;
    public boolean isKeyboardVisible = false;
    private static Context context;
    public View focusedView = null;


    public KeyboardListener(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
    }

    @SimpleFunction(description = "Initialises a KeyboardListener on a Horizontal/Vertical arrangement.")
    public void InitKeyboardListenerHV(HVArrangement arrangement)  {
        initKeyboardListener(arrangement.getView());
    }

    @SimpleFunction(description = "Initialises a KeyboardListener on a Table arrangement.")
    public void InitKeyboardListenerTA(TableArrangement tableArrangement)  {
       initKeyboardListener(tableArrangement.getView());
    }

    @SimpleFunction(description = "Closes/Hides keyboard.")
    public void HideSoftKeyboard() {
        if(getActivity(context).getCurrentFocus()!= null || focusedView != null) {
            IBinder binder = null;
            if(getActivity(context).getCurrentFocus() == null && focusedView != null) {
                focusedView.requestFocus();
                binder = focusedView.getWindowToken();
            }
            else
            {
               binder = getActivity(context).getCurrentFocus().getWindowToken();
            }

            InputMethodManager inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(binder, 0);
        }
    }

    @SimpleFunction(description = "Opens/Shows Keyboard.")
    public void ShowSoftKeyboard() {
        if(getActivity(context).getCurrentFocus()!= null || focusedView != null) {
            IBinder binder = null;
            if(getActivity(context).getCurrentFocus() == null && focusedView != null) {
                focusedView.requestFocus();
                binder = focusedView.getWindowToken();
            }
            else
            {
                binder = getActivity(context).getCurrentFocus().getWindowToken();
            }

            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            inputMethodManager.toggleSoftInputFromWindow(
                    binder,
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @SimpleEvent(description = "Triggers whenever the keyboard is opened.")
    public void OnKeyboardOpen()    {
        EventDispatcher.dispatchEvent(this, "OnKeyboardOpen");
    }

    @SimpleEvent(description = "Triggers whenever the keyboard is closed.")
    public void OnKeyboardClosed()    {
        EventDispatcher.dispatchEvent(this, "OnKeyboardClosed");
    }

    @SimpleProperty(description = "Returns true or false of keyboardOpenState")
    public boolean IsKeyboardOpen()  {
        return isKeyboardVisible;
    }

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    public void initKeyboardListener(View v)
    {
        focusedView = v;
        final SoftKeyboardStateWatcher softKeyboardStateWatcher
                = new SoftKeyboardStateWatcher(v);
        softKeyboardStateWatcher.addSoftKeyboardStateListener(new SoftKeyboardStateWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                isKeyboardVisible = true;
                OnKeyboardOpen();
            }

            @Override
            public void onSoftKeyboardClosed() {
                isKeyboardVisible = false;
                OnKeyboardClosed();
            }
        });
    }
}
