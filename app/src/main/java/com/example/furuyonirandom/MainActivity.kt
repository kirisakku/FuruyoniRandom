package com.example.furuyonirandom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import kotlinx.android.synthetic.main.activity_main.*
import com.example.furuyonirandom.R
import MegamiList.*
import android.graphics.BitmapFactory
import kotlin.random.Random
import java.io.*

class MainActivity : AppCompatActivity() {

    fun getCheckedMegami():MutableList<String> {
        val checkedMegami:MutableList<String> = mutableListOf();
        // チェックがされていたら選択可能なメガミ
        if (yurinaCheckbox.isChecked() == true) {
            checkedMegami.add("ユリナ");
        }
        if (saineCheckbox.isChecked() == true) {
            checkedMegami.add("サイネ");
        }
        if (himikaCheckbox.isChecked() == true) {
            checkedMegami.add("ヒミカ");
        }
        if (tokoyoCheckbox.isChecked() == true) {
            checkedMegami.add("トコヨ");
        }
        if (oboroCheckbox.isChecked() == true) {
            checkedMegami.add("オボロ");
        }
        if (yukihiCheckbox.isChecked() == true) {
            checkedMegami.add("ユキヒ");
        }
        if (shinraCheckbox.isChecked() == true) {
            checkedMegami.add("シンラ");
        }
        if (haganeCheckbox.isChecked() == true) {
            checkedMegami.add("ハガネ");
        }
        if (chikageCheckbox.isChecked() == true) {
            checkedMegami.add("チカゲ");
        }
        if (kururuCheckbox.isChecked() == true) {
            checkedMegami.add("クルル");
        }
        if (sariyaCheckbox.isChecked() == true) {
            checkedMegami.add("サリヤ");
        }
        if (rairaCheckbox.isChecked() == true) {
            checkedMegami.add("ライラ");
        }
        if (utsuroCheckbox.isChecked() == true) {
            checkedMegami.add("ウツロ");
        }
        if (honokaCheckbox.isChecked() == true) {
            checkedMegami.add("ホノカ");
        }
        if (korunuCheckbox.isChecked() == true) {
            checkedMegami.add("コルヌ");
        }
        if (yatsuhaCheckbox.isChecked() == true) {
            checkedMegami.add("ヤツハ");
        }
        if (hatsumiCheckbox.isChecked() == true) {
            checkedMegami.add("ハツミ");
        }
        if (mizukiCheckbox.isChecked() == true) {
            checkedMegami.add("ミズキ");
        }

        return checkedMegami;
    }

    fun getNinzuu(selected: String): Int {
        if (selected == "2人") {
            return 2;
        }
        if (selected == "3人") {
            return 3;
        }
        if (selected == "1人") {
            return 1;
        }

        // 想定外
        return -1;
    }

    fun setText(list: MutableList<Map<String, String>>, selected: String) {
        if (selected == "2人") {
            megami1.text = list[0].get("megamiCategory");
            megami3.text = list[1].get("megamiCategory");
        } else if (selected == "3人") {
            megami1.text = list[0].get("megamiCategory");
            megami2.text = list[1].get("megamiCategory");
            megami3.text = list[2].get("megamiCategory");
        } else if (selected == "1人") {
            megami2.text = list[0].get("megamiCategory");
        }
    }

    fun setImage(list: MutableList<Map<String, String>>, selected: String) {
        val assets = resources.assets;

        var instream1: InputStream? = null;
        var instream2: InputStream? = null;
        var instream3: InputStream? = null;
        var bitmap1: Bitmap? = null;
        var bitmap2: Bitmap? = null;
        var bitmap3: Bitmap? = null;
        // try-with-resources
        try {
            if (selected == "2人") {
                instream1 = assets.open(list[0].get("image").toString());
                instream3 = assets.open(list[1].get("image").toString());
            } else if (selected == "3人") {
                instream1 = assets.open(list[0].get("image").toString());
                instream2 = assets.open(list[1].get("image").toString());
                instream3 = assets.open(list[2].get("image").toString());
            } else if (selected == "1人") {
                instream2 = assets.open(list[0].get("image").toString());
            }

            if (instream1 != null) {
                bitmap1 = BitmapFactory.decodeStream(instream1);
                megamiImage1.setImageBitmap(bitmap1);
            }
            if (instream2 != null) {
                bitmap2 = BitmapFactory.decodeStream(instream2);
                megamiImage2.setImageBitmap(bitmap2);
            }
            if (instream3 != null) {
                bitmap3 = BitmapFactory.decodeStream(instream3);
                megamiImage3.setImageBitmap(bitmap3);
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseButton.setOnClickListener {
            val selected = ninzuu.selectedItem as String;
            val num = this.getNinzuu(selected);
            val checkedMegami = this.getCheckedMegami();

            // 初期化
            megami1.text = "";
            megami2.text = "";
            megami3.text = "";
            megamiImage1.setImageBitmap(null);
            megamiImage2.setImageBitmap(null);
            megamiImage3.setImageBitmap(null);

            // メガミが人数分選べないケース
            if (checkedMegami.size < num) {
                errorMessage.text = "エラー：メガミを" + selected + "選べません";
            } else {
                // 以下はメガミが人数分選べるケース
                val choosenMegamiList:MutableList<Map<String, String>> = mutableListOf();
                val megamiList = getList(this, checkedMegami);
                var nonDuplicatedMegamiList = megamiList.toList();
                errorMessage.text = "";

                for (i in 1..num) {
                    val randomNumber = Random.nextInt(nonDuplicatedMegamiList.size);
                    val targetData = nonDuplicatedMegamiList[randomNumber];
                    choosenMegamiList.add(targetData)

                    // 同じ種類のメガミを除外する
                    nonDuplicatedMegamiList =
                        nonDuplicatedMegamiList.filter { elem -> elem.get("megamiName") != targetData.get("megamiName") }
                }

                // テキスト設定
                this.setText(choosenMegamiList, selected);

                // 画像設定
                this.setImage(choosenMegamiList, selected);
            }
        }
    }
}
