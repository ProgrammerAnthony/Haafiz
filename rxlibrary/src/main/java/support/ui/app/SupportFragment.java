package support.ui.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class SupportFragment extends Fragment {

  protected SupportActivity mSupportActivity;

  private Unbinder mUnbinder;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof SupportActivity) {
      mSupportActivity = (SupportActivity) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mSupportActivity = null;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(getFragmentLayout(), container, false);
  }

  /**
   * Every fragment has to inflate a layout in the onCreateView method. We have added this method
   * to
   * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
   * inflate in this method when extends StarterFragment.
   */
  protected abstract int getFragmentLayout();

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mUnbinder = ButterKnife.bind(this, view);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null) {
      mUnbinder.unbind();
      mUnbinder = null;
    }
  }
}
