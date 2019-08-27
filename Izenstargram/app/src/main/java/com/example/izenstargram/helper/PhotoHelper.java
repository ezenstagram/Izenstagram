package com.example.izenstargram.helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class PhotoHelper {
    //싱글톤 시작
    private static PhotoHelper instance = null;

    public static PhotoHelper getInstance(){
        if(instance==null) instance = new PhotoHelper();

        return instance;
    }
    public static void freeInstance(){
        instance = null;
    }
    private PhotoHelper(){} //생성자가 private 이기 때문에 외부에서 new를 이용해서 사용하지 못한다.
    //싱글톤 끝

    /**
     * DCIM 디렉토리 하위에 새로 저장될 사진 파일의 이름을 생성한다.
     * @return 경로 + 파일이름 문자열
     */
    public String getNewPhotoPath(){
        Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH)+1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        int ss = calendar.get(Calendar.SECOND);

        String fileName = String.format("p%04d-%02d-%02d %02d-%02d-%02d.jpg",
                yy,mm,dd,hh,mi,ss);
        //공용폴더인 DCIM 경로 얻어오기
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if(!dir.exists()){ //폴더가 없으면 만들기
            dir.mkdirs();
        }
        Log.d("[INFO]","dir="+dir);

        String photoPath = dir.getAbsolutePath()+"/"+fileName;
        Log.d("[INFO}"," photoPath = " + photoPath);
        return photoPath;


    }

    /***
     * 큰 이미지를 스마트폰 크기로 줄이기
     * @param activity : MainActivity
     * @param path : 이미지 파일의 경로 + 이름
     * @return Bitmap : 이미지를 리턴
     */
    public Bitmap getThumb(Activity activity, String path){
        //실제 이미지를 저장할 객체
        Bitmap bmp = null;
        //1. 스마트폰의 해상도 얻기
        // 해상도 관리 객체
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //스마트폰의 해상도를 얻어와서, displayMetrics에 저장
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //가로, 세로 크기 얻기
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        //가로, 세로 중에서 긴축을 골라내기
        int maxScale = deviceWidth;
        if(deviceWidth<deviceHeight) maxScale = deviceHeight;
        /*2. 이미지 크기 얻기*/
        //비트맵 이미지 로더의 옵션을 설정하기 위한 객체
        BitmapFactory.Options options = new BitmapFactory.Options();
        //비트맵을 바로 로드하지 말고, 정보만 읽어오라고 설정
        options.inJustDecodeBounds = true;
        //비트맵 파일 읽어오기 - 옵션에 의해서 정보만 읽어온후, options에 저장시킴
        BitmapFactory.decodeFile(path,options);
        //이미지 가로, 세로 얻기
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        //이미지 가로, 세로에서 긴 축 얻기
        int fscale = imageWidth;
        if(imageWidth < imageHeight) fscale = imageHeight;
        /*3. 이미지 리사이징 */
        //이미지의 긴 축의 길이가 스마트폰의 긴 축 길이보다 크면
        if(fscale>maxScale){
            //이미지의 사이즈를 maxScale 로 나누어서 샘플링 사이즈 꼐산
            //ex) 이미지의 긴축이 2400px, 스마트폰의 긴축이 800px 일때
            //2400/800=3 ==>샘플링 사이즈 = 3
            int sampleSize = fscale/maxScale;
            //새 비트맵 옵션 생성
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            //샘플 사이즈 설정 ==> 3으로 지정하면, 1/3크기가 된다.
            options2.inSampleSize = sampleSize;
            //이미지 읽어오기 - sampleSize 만큼 크기를 줄여서 읽어온다.
            bmp = BitmapFactory.decodeFile(path,options2);
        }else{
            //이미지 사이즈가 스마트폰 사이즈를 넘지 않으면, 원본 크기로 읽어온다.
            bmp = BitmapFactory.decodeFile(path);
        }
        /*이미지를 상황에 맞게 회전시킨다.*/
        try {
            // 사진에서 Exif 정보만 가져오기
            ExifInterface exifInterface = new ExifInterface(path);
            //회전각 정보 얻어오기
            int exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            bmp = rotate(bmp, exifDegree); //회전시킬 그림, 몇도를 돌릴것이냐
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmp;

    }


    /**
     * EXIF 정보를 회전각도로 변환하는 메소드
     * @param exifOrientation : EXIF 회전각
     * @return int : 실제 각도
     */
    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            //The Matrix class hods a 3*3 matrix for transforming coordinates
            Matrix m = new Matrix();
            //public void setRoate(float degrees, float px, float py): bitmap 기준, 위치와 각도 설정
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            //설정된 위치기준으로 회전시킨 bitmap  새로 만들기


            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

            if (bitmap != converted) {
                bitmap.recycle();
                bitmap = converted;
            }

        } return bitmap;
    }
}
