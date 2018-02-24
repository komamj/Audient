/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinshang.audient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    /**
     * {"data":{"keyword":"黄家驹","currentPage":1,"currentNumber":147,"items":[{"mediaId":"001yS0N33yPm1B","mediaName":"海阔天空","interval":324,"album":{"mediaId":"002qcJuX3lO3EZ","mediaName":"乐与怒"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"002pKRoX4Qbafa","mediaName":"光辉岁月","interval":298,"album":{"mediaId":"001C2BRX15iE4B","mediaName":"命运派对"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"000DIFHv0PjOHH","mediaName":"不再犹豫","interval":250,"album":{"mediaId":"000DWELJ4Y7U3P","mediaName":"黄家驹原作精选集"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"004Gyue33FERTT","mediaName":"真的爱你","interval":275,"album":{"mediaId":"000eOgmK1fN8Cs","mediaName":"BEYOND IV"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"00247u9f23fivj","mediaName":"谁伴我闯荡","interval":251,"album":{"mediaId":"000DWELJ4Y7U3P","mediaName":"黄家驹原作精选集"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"001mbYZr3QR68r","mediaName":"喜欢你","interval":273,"album":{"mediaId":"001oHxZZ1pAQn4","mediaName":"秘密警察"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"001M0E9Z30ixjI","mediaName":"灰色轨迹","interval":327,"album":{"mediaId":"000DWELJ4Y7U3P","mediaName":"黄家驹原作精选集"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"003Bm0EC2bevBk","mediaName":"大地","interval":264,"album":{"mediaId":"002OfzpN3ZTdf1","mediaName":"4拍4"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"003ZUKRH4CCoei","mediaName":"冷雨夜","interval":302,"album":{"mediaId":"000DWELJ4Y7U3P","mediaName":"黄家驹原作精选集"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"000lIkuJ1CQHpI","mediaName":"情人","interval":317,"album":{"mediaId":"002qcJuX3lO3EZ","mediaName":"乐与怒"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"000nnbeh1CX79k","mediaName":"无尽空虚","interval":279,"album":{"mediaId":"000fRELw0ShGt1","mediaName":"无尽空虚"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"003LEsqM1DZJF7","mediaName":"无悔这一生","interval":235,"album":{"mediaId":"003XXfx92RMtUh","mediaName":"真的见证"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"004ALzmy3bSS31","mediaName":"逝去日子","interval":227,"album":{"mediaId":"000eOgmK1fN8Cs","mediaName":"BEYOND IV"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"00362wdo2VbWvD","mediaName":"曾是拥有","interval":268,"album":{"mediaId":"000eOgmK1fN8Cs","mediaName":"BEYOND IV"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"0007kTiZ1kWBvK","mediaName":"午夜怨曲","interval":272,"album":{"mediaId":"003XXfx92RMtUh","mediaName":"真的见证"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"0029GXbo0FgT5F","mediaName":"再见理想","interval":287,"album":{"mediaId":"001YfPBA0OhnOS","mediaName":"缝纫机乐队 电影原声音乐"},"artist":{"mediaId":"003bD7bY1MBaBg","mediaName":"黄家驹"},"extendProps":null},{"mediaId":"002eaFHp1H0ApQ","mediaName":"农民","interval":317,"album":{"mediaId":"004ex7iR3S9VcR","mediaName":"继续革命"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"0027oAiI4evyih","mediaName":"不可一世","interval":255,"album":{"mediaId":"004ex7iR3S9VcR","mediaName":"继续革命"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"000chkjP2u8C8j","mediaName":"大地","interval":262,"album":{"mediaId":"002V6dOT1me0fJ","mediaName":"大地"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null},{"mediaId":"001ZWACH1NF8QF","mediaName":"旧日的足迹","interval":261,"album":{"mediaId":"002MGTsZ3Zl6JH","mediaName":"现代舞台"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"extendProps":null}]},"code":0,"message":null}
     */
    @SerializedName("data")
    public DataBean dataBean;

    public static class DataBean {
        @SerializedName("items")
        public List<Audient> audients;
    }
}
