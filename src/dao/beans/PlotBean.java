package dao.beans;

import java.awt.Point;
import java.util.List;

public class PlotBean {
	
	private int plotId;
	private String name;
	private int type;
	private int x;
	private int y;
	private int width;
	private int height;
	private int dimension;
	private int prePlotId;
	private int backPlotId;
	
	private int XType;
	private String XName;
	private double XMin;
	private double XMax;
	
	private int YType;
	private String YName;
	private double YMin;
	private double YMax;
	
	private int ZType;
	private String ZName;
	private double ZMin;
	private double ZMax;
	
	private String gateName;
	private List<Point> gatePos;
	private int gateType;
	public int getPlotId() {
		return plotId;
	}
	public void setPlotId(int plotId) {
		this.plotId = plotId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getDimension() {
		return dimension;
	}
	public void setDimension(int dimension) {
		this.dimension = dimension;
	}
	public int getPrePlotId() {
		return prePlotId;
	}
	public void setPrePlotId(int prePlotId) {
		this.prePlotId = prePlotId;
	}
	public int getBackPlotId() {
		return backPlotId;
	}
	public void setBackPlotId(int backPlotId) {
		this.backPlotId = backPlotId;
	}
	public int getXType() {
		return XType;
	}
	public void setXType(int xType) {
		XType = xType;
	}
	public String getXName() {
		return XName;
	}
	public void setXName(String xName) {
		XName = xName;
	}
	public double getXMin() {
		return XMin;
	}
	public void setXMin(double xMin) {
		XMin = xMin;
	}
	public double getXMax() {
		return XMax;
	}
	public void setXMax(double xMax) {
		XMax = xMax;
	}
	public int getYType() {
		return YType;
	}
	public void setYType(int yType) {
		YType = yType;
	}
	public String getYName() {
		return YName;
	}
	public void setYName(String yName) {
		YName = yName;
	}
	public double getYMin() {
		return YMin;
	}
	public void setYMin(double yMin) {
		YMin = yMin;
	}
	public double getYMax() {
		return YMax;
	}
	public void setYMax(double yMax) {
		YMax = yMax;
	}
	public int getZType() {
		return ZType;
	}
	public void setZType(int zType) {
		ZType = zType;
	}
	public String getZName() {
		return ZName;
	}
	public void setZName(String zName) {
		ZName = zName;
	}
	public double getZMin() {
		return ZMin;
	}
	public void setZMin(double zMin) {
		ZMin = zMin;
	}
	public double getZMax() {
		return ZMax;
	}
	public void setZMax(double zMax) {
		ZMax = zMax;
	}
	public String getGateName() {
		return gateName;
	}
	public void setGateName(String gateName) {
		this.gateName = gateName;
	}
	public List<Point> getGatePos() {
		return gatePos;
	}
	public void setGatePos(List<Point> gatePos) {
		this.gatePos = gatePos;
	}
	public int getGateType() {
		return gateType;
	}
	public void setGateType(int gateType) {
		this.gateType = gateType;
	}
	
	

}
