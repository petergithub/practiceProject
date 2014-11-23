package demo.algorithm.sort.util.support;
import demo.algorithm.sort.util.SortUtil;
/**
 * 冒泡排序
 * @author treeroot
 * @since 2006-2-2
 * @version 1.0
 */
public class BubbleSort implements SortUtil.Sort{
    public void sort(int[] data) {
        for(int i=0;i<data.length;i++){
            for(int j=data.length-1;j>i;j--){
                if(data[j]<data[j-1]){
                    SortUtil.swap(data,j,j-1);
                }
            }
        }
    }
}