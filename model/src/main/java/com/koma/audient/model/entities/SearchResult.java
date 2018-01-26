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
package com.koma.audient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    /**
     * {"data":{"keyword":"黄家驹","currentPage":1,"currentNumber":147,"items":[{"id":"001yS0N33yPm1B","name":"海阔天空","interval":324,"album":{"id":"002qcJuX3lO3EZ","name":"乐与怒"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"002pKRoX4Qbafa","name":"光辉岁月","interval":298,"album":{"id":"001C2BRX15iE4B","name":"命运派对"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"000DIFHv0PjOHH","name":"不再犹豫","interval":250,"album":{"id":"000DWELJ4Y7U3P","name":"黄家驹原作精选集"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"004Gyue33FERTT","name":"真的爱你","interval":275,"album":{"id":"000eOgmK1fN8Cs","name":"BEYOND IV"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"00247u9f23fivj","name":"谁伴我闯荡","interval":251,"album":{"id":"000DWELJ4Y7U3P","name":"黄家驹原作精选集"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"001mbYZr3QR68r","name":"喜欢你","interval":273,"album":{"id":"001oHxZZ1pAQn4","name":"秘密警察"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"001M0E9Z30ixjI","name":"灰色轨迹","interval":327,"album":{"id":"000DWELJ4Y7U3P","name":"黄家驹原作精选集"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"003Bm0EC2bevBk","name":"大地","interval":264,"album":{"id":"002OfzpN3ZTdf1","name":"4拍4"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"003ZUKRH4CCoei","name":"冷雨夜","interval":302,"album":{"id":"000DWELJ4Y7U3P","name":"黄家驹原作精选集"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"000lIkuJ1CQHpI","name":"情人","interval":317,"album":{"id":"002qcJuX3lO3EZ","name":"乐与怒"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"000nnbeh1CX79k","name":"无尽空虚","interval":279,"album":{"id":"000fRELw0ShGt1","name":"无尽空虚"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"003LEsqM1DZJF7","name":"无悔这一生","interval":235,"album":{"id":"003XXfx92RMtUh","name":"真的见证"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"004ALzmy3bSS31","name":"逝去日子","interval":227,"album":{"id":"000eOgmK1fN8Cs","name":"BEYOND IV"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"00362wdo2VbWvD","name":"曾是拥有","interval":268,"album":{"id":"000eOgmK1fN8Cs","name":"BEYOND IV"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"0007kTiZ1kWBvK","name":"午夜怨曲","interval":272,"album":{"id":"003XXfx92RMtUh","name":"真的见证"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"0029GXbo0FgT5F","name":"再见理想","interval":287,"album":{"id":"001YfPBA0OhnOS","name":"缝纫机乐队 电影原声音乐"},"artist":{"id":"003bD7bY1MBaBg","name":"黄家驹"},"extendProps":null},{"id":"002eaFHp1H0ApQ","name":"农民","interval":317,"album":{"id":"004ex7iR3S9VcR","name":"继续革命"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"0027oAiI4evyih","name":"不可一世","interval":255,"album":{"id":"004ex7iR3S9VcR","name":"继续革命"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"000chkjP2u8C8j","name":"大地","interval":262,"album":{"id":"002V6dOT1me0fJ","name":"大地"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null},{"id":"001ZWACH1NF8QF","name":"旧日的足迹","interval":261,"album":{"id":"002MGTsZ3Zl6JH","name":"现代舞台"},"artist":{"id":"002pUZT93gF4Cu","name":"BEYOND"},"extendProps":null}]},"code":0,"message":null}
     */
    @SerializedName("data")
    public DataBean dataBean;

    public static class DataBean {
        @SerializedName("items")
        public List<Audient> audients;
    }
}
