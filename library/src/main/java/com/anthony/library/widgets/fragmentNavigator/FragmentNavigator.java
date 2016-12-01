/*Copyright 2016 Aspsine. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.*/

package com.anthony.library.widgets.fragmentNavigator;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Anthony on 2016/8/31.
 * Class Note:
 *
 * An useful fragment navigator helps you control fragments better and easier.
 * You will not need to worry about the dirty things like switch fragments,
 * fragments overlay, save fragments states and restore fragments states.
 * The lib will do them all for you. Fragments in activity and nest Fragment are all supported.
 */
public class FragmentNavigator {

    private static final String EXTRA_CURRENT_POSITION = "extra_current_position";

    private FragmentManager mFragmentManager;

    private FragmentNavigatorAdapter mAdapter;

    @IdRes
    private int mContainerViewId;

    private int mCurrentPosition = -1;

    private int mDefaultPosition;

    public FragmentNavigator(FragmentManager fragmentManager, FragmentNavigatorAdapter adapter, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mAdapter = adapter;
        this.mContainerViewId = containerViewId;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA_CURRENT_POSITION, mDefaultPosition);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_CURRENT_POSITION, mCurrentPosition);
    }

    /**
     * @see #showFragment(int, boolean)
     */
    public void showFragment(int position) {
        showFragment(position, false);
    }

    /**
     * @see #showFragment(int, boolean, boolean)
     */
    public void showFragment(int position, boolean reset) {
        showFragment(position, reset, false);
    }

    /**
     * Show fragment at given position
     *
     * @param position fragment position
     * @param reset true if fragment in given position need reset otherwise false
     * @param allowingStateLoss true if allowing state loss otherwise false
     */
    public void showFragment(int position, boolean reset, boolean allowingStateLoss) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            if (position == i) {
                if (reset) {
                    remove(position, transaction);
                    add(position, transaction);
                } else {
                    show(i, transaction);
                }
            } else {
                hide(i, transaction);
            }
        }
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    /**
     * reset all the fragments and show current fragment
     *
     * @see #resetFragments(int)
     */
    public void resetFragments() {
        resetFragments(mCurrentPosition);
    }

    /**
     * @see #resetFragments(int, boolean)
     */
    public void resetFragments(int position) {
        resetFragments(position, false);
    }

    /**
     * reset all the fragment and show given position fragment
     *
     * @param position fragment position
     * @param allowingStateLoss true if allowing state loss otherwise false
     */
    public void resetFragments(int position, boolean allowingStateLoss) {
        this.mCurrentPosition = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeAll(transaction);
        add(position, transaction);
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    /**
     * @see #removeAllFragment(boolean)
     */
    public void removeAllFragment() {
        removeAllFragment(false);
    }

    /**
     * remove all fragment in the {@link FragmentManager}
     *
     * @param allowingStateLoss true if allowing state loss otherwise false
     */
    public void removeAllFragment(boolean allowingStateLoss) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeAll(transaction);
        if (allowingStateLoss) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }
    }

    /**
     * @return current showing fragment's position
     */
    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * Also @see #getFragment(int)
     *
     * @return current position fragment
     */
    public Fragment getCurrentFragment() {
        return getFragment(mCurrentPosition);
    }

    /**
     * Get the fragment has been added in the given position. Return null if the fragment
     * hasn't been added in {@link FragmentManager} or has been removed already.
     *
     * @param position position of fragment in {@link FragmentNavigatorAdapter#onCreateFragment(int)}}
     *                 and {@link FragmentNavigatorAdapter#getTag(int)}
     * @return The fragment if found or null otherwise.
     */
    public Fragment getFragment(int position) {
        String tag = mAdapter.getTag(position);
        return mFragmentManager.findFragmentByTag(tag);
    }

    private void show(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            add(position, transaction);
        } else {
            transaction.show(fragment);
        }
    }

    private void hide(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.hide(fragment);
        }
    }

    private void add(int position, FragmentTransaction transaction) {
        Fragment fragment = mAdapter.onCreateFragment(position);
        String tag = mAdapter.getTag(position);
        transaction.add(mContainerViewId, fragment, tag);
    }

    private void removeAll(FragmentTransaction transaction) {
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            remove(i, transaction);
        }
    }

    private void remove(int position, FragmentTransaction transaction) {
        String tag = mAdapter.getTag(position);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            transaction.remove(fragment);
        }
    }

    public void setDefaultPosition(int defaultPosition) {
        this.mDefaultPosition = defaultPosition;
        if (mCurrentPosition == -1) {
            this.mCurrentPosition = defaultPosition;
        }
    }
}
