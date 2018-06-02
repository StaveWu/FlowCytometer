package dao.implementions;

import java.awt.Point;
import java.awt.Rectangle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.interfaces.IPlotsDAO;
import plot.DensityPlot;
import plot.DotPlot;
import plot.Histogram;
import plot.Plot;
import plot.axis.Axis;
import plot.axis.AxisAlgebra;
import plot.axis.AxisDimension;
import plot.axis.AxisType;
import plot.gate.Gate;
import plot.gate.HorizontalGate;
import plot.gate.PolygonGate;
import plot.gate.RectangleGate;
import plot.gate.VerticalGate;
import plot.gate.Vertice;

public class PlotsDAOImp implements IPlotsDAO {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	
	public PlotsDAOImp(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void createTable(String caret) throws SQLException {
		String plotsql = "create table if not exists "
				+ "plot" + caret + "(" + 
				"id int not null primary key," + 
				"name varchar(100)," + 
				"type varchar(20)," + 
				"x int," + 
				"y int," + 
				"width int," + 
				"height int," + 
				"dimension int," + 
				"previd int," + 
				"nextid int" + 
				");";
		pstmt = conn.prepareStatement(plotsql);
		pstmt.execute();
		String axissql = "create table if not exists "
				+ "axis" + caret + "(" + 
				"plotid int not null," + 
				"name varchar(100)," + 
				"type varchar(20)," + 
				"algebra varchar(20)," + 
				"min double," + 
				"max double" + 
				");";
		pstmt = conn.prepareStatement(axissql);
		pstmt.execute();
		String gatesql = "create table if not exists "
				+ "gate" + caret + "(" + 
				"plotid int not null," + 
				"name varchar(100)," + 
				"type varchar(20)," + 
				"dotorder int," + 
				"x int," + 
				"y int" + 
				");";
		pstmt = conn.prepareStatement(gatesql);
		pstmt.execute();
		pstmt.close();
	}

	@Override
	public List<Plot> findAll(String caret) throws SQLException {
		List<Plot> res = new ArrayList<>();
		for (Plot plot : generatePlot(caret, "DotPlot")) {
			res.add(plot);
		}
		for (Plot plot : generatePlot(caret, "DensityPlot")) {
			res.add(plot);
		}
		for (Plot plot : generatePlot(caret, "Histogram")) {
			res.add(plot);
		}
		return res;
	}
	
	private List<Plot> generatePlot(String caret, String plotType) throws SQLException {
		List<Plot> res = new ArrayList<>();
		String plotsql = "select * from plot" + caret + " where type = " + "'" + plotType + "'";
		pstmt = conn.prepareStatement(plotsql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Plot plot = null;
			if (plotType.equals("DotPlot")) {
				plot = new DotPlot();
			} else if (plotType.equals("Histogram")) {
				plot = new Histogram();
			} else if (plotType.equals("DensityPlot")) {
				plot = new DensityPlot();
			} else {
				return res;
			}
			plot.setUid(rs.getInt(1));
			plot.setPlotName(rs.getString(2));
			plot.setBounds(rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
			plot.setPrevid(rs.getInt(9));
			plot.setNextid(rs.getInt(10));
			
			String axissql = "select * from axis" + caret + " where plotid = '" + plot.getUid() + "'";
			pstmt = conn.prepareStatement(axissql);
			ResultSet rs2 = pstmt.executeQuery();
			List<Axis> axes = new ArrayList<>();
			while (rs2.next()) {
				Axis axis = new Axis();
				axis.setName(rs2.getString(2));
				axis.setMinValue(rs2.getDouble(5));
				axis.setMaxValue(rs2.getDouble(6));
				String type = rs2.getString(3);
				String algebra = rs2.getString(4);
				if (type.equals("x2d")) {
					axis.setTypeStrategy(AxisType.X);
					axis.setDimensionStrategy(AxisDimension.X2D);
				} else if (type.equals("y2d")) {
					axis.setTypeStrategy(AxisType.Y);
					axis.setDimensionStrategy(AxisDimension.Y2D);
				}
				if (algebra.equals("log")) {
					axis.setAlgebraStrategy(AxisAlgebra.LOG);
				} else {
					axis.setAlgebraStrategy(AxisAlgebra.LINEAR);
				}
				axes.add(axis);
			}
			plot.setAxis(axes);
			
			String gatesql = "select * from gate" + caret + " where plotid = '" + plot.getUid() + "' order by dotorder";
			pstmt = conn.prepareStatement(gatesql);
			ResultSet rs3 = pstmt.executeQuery();
			int count = 0;
			Gate gate = null;
			while (rs3.next()) {
				if (count == 0) {
					String type = rs3.getString(3);
					if (type.equals("R")) {
						gate = new RectangleGate();
					} else if (type.equals("P")) {
						gate = new PolygonGate();
					} else if (type.equals("H")) {
						gate = new HorizontalGate((Rectangle) plot.getDataRegion());
					} else if (type.equals("V")) {
						gate = new VerticalGate((Rectangle) plot.getDataRegion());
					} else {
						return res;
					}
					gate.setName(rs3.getString(2));
				}
				Point p = new Point(rs3.getInt(5), rs3.getInt(6));
				gate.addVertice(new Vertice(p));
				count++;
			}
			plot.setGate(gate);
			res.add(plot);
		}
		pstmt.close();
		return res;
	}

	@Override
	public void addAll(String caret, List<Plot> plots) throws SQLException {
		// 先清空数据库
		String deletePlotsql = "delete from plot" + caret;
		pstmt = conn.prepareStatement(deletePlotsql);
		pstmt.execute();
		String deleteAxissql = "delete from axis" + caret;
		pstmt = conn.prepareStatement(deleteAxissql);
		pstmt.execute();
		String deleteGatesql = "delete from gate" + caret;
		pstmt = conn.prepareStatement(deleteGatesql);
		pstmt.execute();
		
		// 再添加信息
		for (Plot plot : plots) {
			// 添加图
			String plotsql = "insert into plot" + caret
					+ "(id,name,type,x,y,width,height,dimension,previd,nextid)"
					+ " values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(plotsql);
			pstmt.setInt(1, plot.getUid());
			pstmt.setString(2, plot.getPlotName());
			pstmt.setString(3, plot.getPlotType());
			pstmt.setInt(4, plot.getX());
			pstmt.setInt(5, plot.getY());
			pstmt.setInt(6, plot.getWidth());
			pstmt.setInt(7, plot.getHeight());
			pstmt.setInt(8, plot.getAxis().length);
			pstmt.setInt(9, plot.getPrevid());
			pstmt.setInt(10, plot.getNextid());
			pstmt.executeUpdate();
		}
		
		
		for (Plot plot : plots) {
			// 添加轴
			String axissql = "insert into axis" + caret
					+ "(plotid,name,type,algebra,min,max) values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(axissql);
			Axis[] axes = plot.getAxis();
			for (Axis ele : axes) {
				pstmt.setInt(1, plot.getUid());
				pstmt.setString(2, ele.getName());
				pstmt.setDouble(5, ele.getMinValue());
				pstmt.setDouble(6, ele.getMaxValue());
				if (ele.getAlgebraStrategy() == AxisAlgebra.LOG) {
					pstmt.setString(4, "log");
				} else {
					pstmt.setString(4, "linear");
				}
				if (plot.getDimension() == 2) {
					if (ele.getTypeStrategy() == AxisType.X) {
						pstmt.setString(3, "x2d");
					} else {
						pstmt.setString(3, "y2d");
					}
				} else {
					if (ele.getTypeStrategy() == AxisType.X) {
						pstmt.setString(3, "x3d");
					} else if (ele.getTypeStrategy() == AxisType.Y) {
						pstmt.setString(3, "y3d");
					} else {
						pstmt.setString(3, "z3d");
					}
				}
				pstmt.executeUpdate();
			}
			// 添加gate
			Gate gate = plot.getGate();
			if (gate != null) {
				String gatesql = "insert into gate" + caret
						+ "(plotid,name,type,dotorder,x,y) values(?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(gatesql);
				pstmt.setInt(1, plot.getUid());
				pstmt.setString(2, gate.getName());
				pstmt.setString(3, gate.getGateType());
				List<Vertice> vertices = gate.getVertices();
				int i = 1;
				for (Vertice ele : vertices) {
					Point p = ele.getPoint();
					pstmt.setInt(4, i);
					pstmt.setInt(5, p.x);
					pstmt.setInt(6, p.y);
					pstmt.executeUpdate();
					i++;
				}
				if (gate.getGateType().equals("P")) {
					// 如果是多边形，则再添加一个点
					pstmt.setInt(4, i);
					pstmt.setInt(5, vertices.get(0).getPoint().x);
					pstmt.setInt(6, vertices.get(0).getPoint().y);
					pstmt.executeUpdate();
				}
			}
		}
		pstmt.close();
	}

	@Override
	public boolean isExist(String caret) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
