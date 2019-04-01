// Generated code from Butter Knife. Do not modify!
package com.project.EarthFoundation;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyProfileActivity_ViewBinding implements Unbinder {
  private MyProfileActivity target;

  @UiThread
  public MyProfileActivity_ViewBinding(MyProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MyProfileActivity_ViewBinding(MyProfileActivity target, View source) {
    this.target = target;

    target._email = Utils.findRequiredViewAsType(source, R.id.p_email, "field '_email'", TextView.class);
    target._phoneText = Utils.findRequiredViewAsType(source, R.id.phoneNo, "field '_phoneText'", EditText.class);
    target._countryText = Utils.findRequiredViewAsType(source, R.id.country, "field '_countryText'", EditText.class);
    target._stateText = Utils.findRequiredViewAsType(source, R.id.state, "field '_stateText'", EditText.class);
    target._cityText = Utils.findRequiredViewAsType(source, R.id.city, "field '_cityText'", EditText.class);
    target._pinCodeText = Utils.findRequiredViewAsType(source, R.id.pincode, "field '_pinCodeText'", EditText.class);
    target._aadharText = Utils.findRequiredViewAsType(source, R.id.aadhar, "field '_aadharText'", EditText.class);
    target._updatebtn = Utils.findRequiredViewAsType(source, R.id.btnSubmitProfile, "field '_updatebtn'", Button.class);
    target._imageBar = Utils.findRequiredViewAsType(source, R.id.profile_bar2, "field '_imageBar'", ProgressBar.class);
    target._editProfile = Utils.findRequiredViewAsType(source, R.id.EditProfile, "field '_editProfile'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._email = null;
    target._phoneText = null;
    target._countryText = null;
    target._stateText = null;
    target._cityText = null;
    target._pinCodeText = null;
    target._aadharText = null;
    target._updatebtn = null;
    target._imageBar = null;
    target._editProfile = null;
  }
}
