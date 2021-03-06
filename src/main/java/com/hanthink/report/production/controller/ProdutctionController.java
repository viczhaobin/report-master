package com.hanthink.report.production.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Grid;
import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Timeline;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.SplitLine;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.AxisType;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.LineData;
import com.github.abel533.echarts.json.GsonUtil;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Series;
import com.hanthink.report.production.service.ProductionReportService;

/**
* @author 作者 zhaobin
* @version 创建时间：2018年8月23日 上午12:33:03
* 类说明
*/

@Controller
public class ProdutctionController {
	
	@Resource
	private ProductionReportService productionReportService;
	
	@RequestMapping("/production_summery")
    public String productionSummery() {
		
        return "production/production_summery";
    }
	
	
	@RequestMapping("/getSelectProductionSummery")
	@ResponseBody
	public String getSelectProductionSummery() {
		//获取数据
		
		int dataMap[][][] = new int[3][12][31];
		
		HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("cal_type", "daily");
        
        ArrayList<HashMap> rs;
        try {
        	rs = productionReportService.selectProductionSummery(map);
	        if (rs != null && rs.size()>0) {
	        	for(int i=0;i<rs.size();i++) {
	        		HashMap rsi = rs.get(i);
	        		String target = (String) rsi.get("target");
	        		
	        		String calTimeStr = (String) rsi.get("cal_time");
	        		int monthNum = Integer.parseInt(calTimeStr.substring(4, 6));
	        		int dayNum = Integer.parseInt(calTimeStr.substring(6, 8));
	        		int actualQty = ((Integer) rsi.get("actual_qty")).intValue();
	        		
		        	if("BS".equals(target))
		        	{
		        		dataMap[0][monthNum-1][dayNum-1] = actualQty;
		        	} else if ("PS".equals(target)) {
		        		dataMap[1][monthNum-1][dayNum-1] = actualQty;
		        	} else if ("TFS".equals(target)) {
		        		dataMap[2][monthNum-1][dayNum-1] = actualQty;
		        	}
	        	}
	        }
        }
        catch(Exception e){
        	System.out.println("exception:"+e.getMessage());
        	
        }
        
		//设置echart属性
        Option option = new Option();
        
        Option baseOption = new Option();
		
		Timeline timeLine = new Timeline();
		timeLine.axisType(AxisType.category);
		timeLine.playInterval(1000);
		timeLine.data("2018-01-01","2018-02-01","2018-03-01","2018-04-01",
				"2018-05-01", "2018-06-01","2018-07-01","2018-08-01","2018-09-01",
				new LineData("2018-10-01", "diamond", 16),"2018-11-01","2018-12-01");
		baseOption.timeline(timeLine);
		
		Title title = new Title();
		title.subtext("production count");
		baseOption.title(title);
		
		baseOption.tooltip();

		Legend legend = new Legend();
		legend.x(X.right);
		legend.data("BS","PS","TFS");
		legend.selected("PS", false);
		legend.selected("TFS", false);
		baseOption.legend(legend);
		
		baseOption.calculable(true);
		
		Grid grid = new Grid();
		grid.top(80);
		grid.bottom(100);
		Tooltip gToolTip = new Tooltip();
		gToolTip.trigger(Trigger.axis);
		AxisPointer axisPointer = new AxisPointer();
		axisPointer.type(PointerType.shadow);
		axisPointer.show(true);
		gToolTip.axisPointer(axisPointer);
		baseOption.grid(grid);
		baseOption.tooltip(gToolTip);
		
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.axisLabel(new AxisLabel().interval(0));
		xAxis.data("1","\n2","3","\n4","5","\n6","7","\n8",
				"9","\n10","11","\n12","13","\n14","15","\n16",
				"17","\n18","19","\n20","21","\n22","23","\n24",
				"25","\n26","27","\n28","29","\n30","31");
		xAxis.splitLine(new SplitLine().show(false));
		
		ValueAxis yAxis = new ValueAxis();
		yAxis.name("actual Quantity");
		baseOption.xAxis(xAxis);
		baseOption.yAxis(yAxis);
		
		baseOption.series(new Bar("BS"),new Bar("PS"),new Bar("TFS"));
		
		List<Option> options = new ArrayList<>();
		for(int i=0;i<12;i++)
		{
			Option _option = new Option();
			_option.title("2018-" + i + " Production Summery");
			
			Bar bar = new Bar();
			
			bar.name("BS").data(dataMap[0][i][0],dataMap[0][i][1],dataMap[0][i][2],dataMap[0][i][3],dataMap[0][i][4],dataMap[0][i][5],
					dataMap[0][i][6],dataMap[0][i][7],dataMap[0][i][8],dataMap[0][i][9],dataMap[0][i][10],dataMap[0][i][11],
					dataMap[0][i][12],dataMap[0][i][13],dataMap[0][i][14],dataMap[0][i][15],dataMap[0][i][16],dataMap[0][i][17],
					dataMap[0][i][18],dataMap[0][i][19],dataMap[0][i][20],dataMap[0][i][21],dataMap[0][i][22],dataMap[0][i][23],
					dataMap[0][i][24],dataMap[0][i][25],dataMap[0][i][26],dataMap[0][i][27],dataMap[0][i][28],dataMap[0][i][29],dataMap[0][i][30]
					);
			_option.series(bar);
			
			bar = new Bar();
			bar.name("PS").data(dataMap[1][i][0],dataMap[1][i][1],dataMap[1][i][2],dataMap[1][i][3],dataMap[1][i][4],dataMap[1][i][5],
					dataMap[1][i][6],dataMap[1][i][7],dataMap[1][i][8],dataMap[1][i][9],dataMap[1][i][10],dataMap[1][i][11],
					dataMap[1][i][12],dataMap[1][i][13],dataMap[1][i][14],dataMap[1][i][15],dataMap[1][i][16],dataMap[1][i][17],
					dataMap[1][i][18],dataMap[1][i][19],dataMap[1][i][20],dataMap[1][i][21],dataMap[1][i][22],dataMap[1][i][23],
					dataMap[1][i][24],dataMap[1][i][25],dataMap[1][i][26],dataMap[1][i][27],dataMap[1][i][28],dataMap[1][i][29],dataMap[1][i][30]
					);
			_option.series(bar);
			
			bar = new Bar();
			bar.name("TFS").data(dataMap[2][i][0],dataMap[2][i][1],dataMap[2][i][2],dataMap[2][i][3],dataMap[2][i][4],dataMap[2][i][5],
					dataMap[2][i][6],dataMap[2][i][7],dataMap[2][i][8],dataMap[2][i][9],dataMap[2][i][10],dataMap[2][i][11],
					dataMap[2][i][12],dataMap[2][i][13],dataMap[2][i][14],dataMap[2][i][15],dataMap[2][i][16],dataMap[2][i][17],
					dataMap[2][i][18],dataMap[2][i][19],dataMap[2][i][20],dataMap[2][i][21],dataMap[2][i][22],dataMap[2][i][23],
					dataMap[2][i][24],dataMap[2][i][25],dataMap[2][i][26],dataMap[2][i][27],dataMap[2][i][28],dataMap[2][i][29],dataMap[2][i][30]
					);
			_option.series(bar);
			
			options.add(_option);
		}
		option.baseOption(baseOption);
		
		option.options(options);
		
		String optionJson = GsonUtil.format(option); 
		
		return optionJson;
	}
}
