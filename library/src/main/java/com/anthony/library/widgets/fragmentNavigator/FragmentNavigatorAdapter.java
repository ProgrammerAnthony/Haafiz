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

import android.support.v4.app.Fragment;

/**
 * Created by Anthony on 2016/8/31.
 * Class Note:
 */
public interface FragmentNavigatorAdapter {

    Fragment onCreateFragment(int position);

    String getTag(int position);

    int getCount();
}
