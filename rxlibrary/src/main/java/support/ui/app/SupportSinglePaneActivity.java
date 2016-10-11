package support.ui.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.anthony.rxlibrary.R;


/**
 * A {@link AppCompatActivity} that simply contains a single fragment. The intent used to invoke
 * this
 * activity is forwarded to the fragment as arguments during fragment instantiation. Derived
 * activities should only need to implement {@link SupportSinglePaneActivity#onCreatePane()}.
 */
public abstract class SupportSinglePaneActivity extends SupportActivity {
  private Fragment mFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentViewResId());

    if (getIntent().hasExtra(Intent.EXTRA_TITLE)) {
      setTitle(getIntent().getStringExtra(Intent.EXTRA_TITLE));
    }

    final String customTitle = getIntent().getStringExtra(Intent.EXTRA_TITLE);
    setTitle(customTitle != null ? customTitle : getTitle());

    if (savedInstanceState == null) {
      mFragment = onCreatePane();
      mFragment.setArguments(intentToFragmentArguments(getIntent()));
      getSupportFragmentManager().beginTransaction()
          .add(R.id.root_container, mFragment, "single_pane")
          .commit();
    } else {
      mFragment = getSupportFragmentManager().findFragmentByTag("single_pane");
    }
  }

  protected int getContentViewResId() {
    return R.layout.support_ui_singlepane_empty;
  }

  /**
   * Called in <code>onCreate</code> when the fragment constituting this activity is needed.
   * The returned fragment's arguments will be set to the intent used to invoke this activity.
   */
  protected abstract Fragment onCreatePane();

  public Fragment getFragment() {
    return mFragment;
  }

  /**
   * Converts an intent into a {@link Bundle} suitable for use as fragment arguments.
   */
  public static Bundle intentToFragmentArguments(Intent intent) {
    Bundle arguments = new Bundle();
    if (intent == null) {
      return arguments;
    }

    final Uri data = intent.getData();
    if (data != null) {
      arguments.putParcelable("_uri", data);
    }

    final Bundle extras = intent.getExtras();
    if (extras != null) {
      arguments.putAll(intent.getExtras());
    }

    return arguments;
  }

  /**
   * Converts a fragment arguments bundle into an intent.
   */
  public static Intent fragmentArgumentsToIntent(Bundle arguments) {
    Intent intent = new Intent();
    if (arguments == null) {
      return intent;
    }

    final Uri data = arguments.getParcelable("_uri");
    if (data != null) {
      intent.setData(data);
    }

    intent.putExtras(arguments);
    intent.removeExtra("_uri");
    return intent;
  }
}
