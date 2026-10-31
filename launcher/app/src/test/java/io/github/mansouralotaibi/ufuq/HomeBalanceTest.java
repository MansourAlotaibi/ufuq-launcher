package io.github.mansouralotaibi.ufuq;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import static org.junit.Assert.*;
/** Framework geometry checks, not actual TV screenshots or physical remote proof. */
@RunWith(org.robolectric.RobolectricTestRunner.class)
@org.robolectric.annotation.Config(sdk={19,28,34},application=Application.class,qualifiers="en-rUS-land-xhdpi")
public class HomeBalanceTest {
 @Test public void palettesShareBalancedForegroundAndFullScreenBackground(){
  Context c=RuntimeEnvironment.getApplication();
  for(int theme=0;theme<3;theme++){
   UiTheme.select(c,theme);
   try(ActivityController<MainActivity> controller=Robolectric.buildActivity(MainActivity.class).setup()){
    MainActivity a=controller.get();View root=a.findViewById(R.id.root);
    float density=a.getResources().getDisplayMetrics().density;
    assertEquals(Math.round(58*density),((ViewGroup.MarginLayoutParams)a.findViewById(R.id.cinematicClockPanel).getLayoutParams()).topMargin);
    assertEquals(Math.round(148*density),((ViewGroup.MarginLayoutParams)a.findViewById(R.id.reflectionHost).getLayoutParams()).topMargin);
    assertEquals(Math.round(394*density),((ViewGroup.MarginLayoutParams)a.findViewById(R.id.smartCards).getLayoutParams()).topMargin);
    View content=(View)a.findViewById(R.id.homeHeader).getParent();assertEquals(Math.round(50*density),content.getPaddingTop());
    assertEquals(0f,root.getTranslationY(),0f);
   }
  }
 }
 @Test @org.robolectric.annotation.Config(qualifiers="ar-rSA-land-xhdpi")
 public void arabicUsesTheSameBalancedGeometry(){palettesShareBalancedForegroundAndFullScreenBackground();}
 @Test public void actualShelfDecorationCentersFourTilesInBothDirections(){
  for(boolean rtl:new boolean[]{false,true}){
   try(ActivityController<MainActivity> controller=Robolectric.buildActivity(MainActivity.class).setup()){
    MainActivity a=controller.get();
    org.robolectric.util.ReflectionHelpers.setField(a,"rtlAppGrid",rtl);
    androidx.recyclerview.widget.RecyclerView original=a.findViewById(R.id.grid);
    androidx.recyclerview.widget.RecyclerView shelf=new androidx.recyclerview.widget.RecyclerView(a);
    shelf.setLayoutDirection(rtl?View.LAYOUT_DIRECTION_RTL:View.LAYOUT_DIRECTION_LTR);
    androidx.recyclerview.widget.LinearLayoutManager lm=new androidx.recyclerview.widget.LinearLayoutManager(a,androidx.recyclerview.widget.RecyclerView.HORIZONTAL,false);
    shelf.setLayoutManager(lm);shelf.addItemDecoration(original.getItemDecorationAt(0));
    float density=a.getResources().getDisplayMetrics().density;
    int scale=org.robolectric.util.ReflectionHelpers.getField(a,"shelfScalePercent");
    int width=1920,cell=ShelfGeometry.cell(width,density,scale);
    assertTrue(ShelfGeometry.slots(width,density,scale)>4);
    shelf.setAdapter(new androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(){
     public int getItemCount(){return 4;}
     public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int type){
      View tile=new View(a);tile.setLayoutParams(new androidx.recyclerview.widget.RecyclerView.LayoutParams(cell,100));
      return new androidx.recyclerview.widget.RecyclerView.ViewHolder(tile){};
     }
     public void onBindViewHolder(androidx.recyclerview.widget.RecyclerView.ViewHolder holder,int position){}
    });
    shelf.measure(View.MeasureSpec.makeMeasureSpec(width,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(200,View.MeasureSpec.EXACTLY));shelf.layout(0,0,width,200);
    int left=Integer.MAX_VALUE,right=0;
    for(int i=0;i<shelf.getChildCount();i++){View v=shelf.getChildAt(i);left=Math.min(left,v.getLeft());right=Math.max(right,v.getRight());}
    assertEquals(4,shelf.getChildCount());assertEquals(width,left+right,6);
   }
  }
 }
}
