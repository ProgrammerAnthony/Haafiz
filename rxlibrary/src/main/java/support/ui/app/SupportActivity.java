package support.ui.app;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SupportActivity extends AppCompatActivity {

  private Unbinder mUnbinder;

  @Override
  public void setContentView(@LayoutRes int layoutResID) {
    super.setContentView(layoutResID);
    mUnbinder = ButterKnife.bind(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mUnbinder != null) {
      mUnbinder.unbind();
      mUnbinder = null;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }
}
