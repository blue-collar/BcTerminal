package com.telewave.battlecommand.contract;

/**
 * Note:
 * Created by liwh on 2018/12/8,9:32.
 */

public interface FragmentContract {
    /**
     * Fragment to Activity
     */
    interface F2A {
        /**
         * 选择一个页面
         *
         * @param index 选择对应的页面
         */
        void selectViewPagerIndex(int index);

    }

    interface A2F {
        /**
         * 选择一个灾情
         *
         * @param eventSign
         */
        void selectOneDisaster(String eventSign, boolean isHistory);

        void loadingPreviousPage();

        void loadingNextPage();

        void loadingHomePage();

        void loadingEndPage();

    }


}
