package controller.nhanKhau;

import entity.HoKhau;
import entity.NhanKhau;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import utility.DbUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utility.SQLCommand.HO_KHAU_QUERY_LAY_THONG_TIN;
import static utility.SQLCommand.NHAN_KHAU_QUERY_LAY_THONG_TIN;

public class NhanKhauController implements Initializable {
    @FXML
    private TableView<NhanKhau>table;
    @FXML
    private TableColumn<NhanKhau,Integer>  idColumn;
    @FXML
    private TableColumn<NhanKhau,String>  hoTenColumn;
    @FXML
    private TableColumn<NhanKhau,String>  ngaySinhColumn;
    @FXML
    private TableColumn<NhanKhau,String>  gioiTinhColumn;
    @FXML
    private TableColumn<NhanKhau,String>  CMNDColumn;
    @FXML
    private TableColumn<NhanKhau,String>  trangThaiColumn;
    @FXML
    private Button themNhanKhau;
    @FXML
    private ComboBox truongTraCuuF;
    @FXML
    private TextField duLieuF;
    @FXML
    private ImageView confirmF;
    @FXML
    ObservableList<NhanKhau>  nhanKhauList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<HoKhau> hokhauList = FXCollections.observableArrayList();
    private String query = null;
    private String query_hoTen=null;
    private String query_CMND=null;
    private String query_trangThai=null;
    private String query_nguyenQuan=null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    NhanKhau nhanKhau = null ;
    private String truongTraCuu=null;
    private String duLieuTraCuu=null;
    private int id_NK;

    @FXML
    private void Select(ActionEvent event) {
        truongTraCuu = truongTraCuuF.getSelectionModel().getSelectedItem().toString();

    }
    @SneakyThrows
    @FXML
    private void findF(MouseEvent event) {

        try {
            nhanKhauList.clear();
            duLieuTraCuu=duLieuF.getText();
            query_hoTen="SELECT * FROM `nhan_khau` WHERE hoTen like '%" + duLieuTraCuu+"%'";
            query_CMND="SELECT * FROM `nhan_khau` WHERE cmnd like '%" + duLieuTraCuu+"%'";
            query_trangThai="SELECT * FROM `nhan_khau` WHERE trangThai like '%" + duLieuTraCuu+"%'";
            query_nguyenQuan="SELECT * FROM `nhan_khau` WHERE ngaySinh like '%" + duLieuTraCuu+"%'";
            if(truongTraCuu=="H??? t??n"){
                preparedStatement = connection.prepareStatement(query_hoTen);
            } else if(truongTraCuu=="Ch???ng minh nh??n d??n"){
                preparedStatement = connection.prepareStatement(query_CMND);
            }else if(truongTraCuu=="Tr???ng th??i"){
                preparedStatement = connection.prepareStatement(query_trangThai);
            }else if(truongTraCuu=="Ng??y sinh"){
                preparedStatement = connection.prepareStatement(query_nguyenQuan);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setContentText("B???n c???n ch???n tr?????ng tra c???u");
                alert.showAndWait();
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String bieuDienNgaySinh = resultSet.getString("ngaySinh").substring(8)+"-"+resultSet.getString("ngaySinh").substring(5,7)+"-"+resultSet.getString("ngaySinh").substring(0,4);
                nhanKhauList.add(new NhanKhau(
                        resultSet.getInt("idNhanKhau"),
                        resultSet.getString("hoTen"),
                        bieuDienNgaySinh,
                        resultSet.getString("gioiTinh"),
                        resultSet.getString("CMND"),
                        resultSet.getString("trangThai")));
                table.setItems(nhanKhauList);

            }


        } catch (SQLException ex) {
            Logger.getLogger(NhanKhauController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void refreshTable() {
        try {
            nhanKhauList.clear();


            preparedStatement = connection.prepareStatement(NHAN_KHAU_QUERY_LAY_THONG_TIN);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String bieuDienNgaySinh = resultSet.getString("ngaySinh").substring(8)+"-"+resultSet.getString("ngaySinh").substring(5,7)+"-"+resultSet.getString("ngaySinh").substring(0,4);
                nhanKhauList.add(new NhanKhau(
                        resultSet.getInt("idNhanKhau"),
                        resultSet.getString("hoTen"),
                        bieuDienNgaySinh,
                        resultSet.getString("gioiTinh"),
                        resultSet.getString("CMND"),
                        resultSet.getString("trangThai")));
                table.setItems(nhanKhauList);

            }


        } catch (SQLException ex) {
            Logger.getLogger(NhanKhauController.class.getName()).log(Level.SEVERE, null, ex);
        }



    }
    private void loadData() throws SQLException {
        connection = DbUtil.getInstance().getConnection();
        refreshTable();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        hoTenColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        ngaySinhColumn.setCellValueFactory(new PropertyValueFactory<>("bieuDienNgaySinh"));
        gioiTinhColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        CMNDColumn.setCellValueFactory(new PropertyValueFactory<>("CMND"));
        trangThaiColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        table.setItems(nhanKhauList);
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> listTruongTraCuu = FXCollections.observableArrayList("H??? t??n","Ch???ng minh nh??n d??n","Tr???ng th??i","Ng??y sinh");
        truongTraCuuF.setItems(listTruongTraCuu);

        loadData();
    }



    public void changScenceThemNhanKhau(ActionEvent e) throws IOException {



        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/themNhanKhau.fxml"));
        Parent thongTinNK = loader.load();

        Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Th??ng tin nh??n kh???u");
        Scene scene = new Scene(thongTinNK);
        stage.setScene(scene);
        stage.show();

    }



    public void detailNhanKhau(ActionEvent e) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/chiTietNhanKhau.fxml"));
        Parent thongTinNK = loader.load();
        ThongTinNhanKhauController controller = loader.getController();
        NhanKhau selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }
        controller.setNhanKhau(selected);
        Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Th??ng tin nh??n kh???u");
        Scene scene = new Scene(thongTinNK);
        stage.setScene(scene);
        stage.show();
    }



    public void chinhSuaNK(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/chinhSuaNhanKhau.fxml"));
        Parent chinhSuaNKView = loader.load();
        ChinhSuaNhanKhauController controller = loader.getController();
        NhanKhau selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }
        controller.setChinhSuaNK(selected);
        Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("CH???NH S???A NH??N KH???U");
        Scene scene = new Scene(chinhSuaNKView);
        stage.setScene(scene);
        stage.show();
    }


    public void XoaNK(ActionEvent e) throws IOException {
        try {
            int flag=0;
            NhanKhau userlist = table.getSelectionModel().getSelectedItem();
            if (userlist == null) {
                Alert m = new Alert(Alert.AlertType.INFORMATION);
                m.setTitle("Th??ng b??o!");
                m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
                m.setContentText("Vui l??ng ch???n l???i.");
                m.show();
                return;
            }
            id_NK=userlist.getId();



            connection = DbUtil.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(HO_KHAU_QUERY_LAY_THONG_TIN);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(resultSet.getInt("idChuHo")==id_NK) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText(null);
                    alert.setContentText("B???n ??ang x??a m???t ch??? h???, vui l??ng ?????i ch??? h??? tr?????c khi x??a!");
                    alert.showAndWait();
                    flag=1;
                 }
            }
            if(flag==0){

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("X??a nh??n kh???u");
                alert.setHeaderText("B???n c?? th???c s??? mu???n x??a nh??n kh???u n??y ?");
                alert.setContentText("Vi???c x??a nh??n kh???u s??? l??m m???t t???t c??? c??c d??? li???u li??n quan ?????n nh??n kh???u.");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == null) {

                } else if (option.get() == ButtonType.OK) {
                    nhanKhau = table.getSelectionModel().getSelectedItem();

                    connection = DbUtil.getInstance().getConnection();
                    query = "DELETE FROM `nhan_khau` WHERE idNhanKHau ="+nhanKhau.getId();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();
                } else if (option.get() == ButtonType.CANCEL) {

                }
            }



        } catch (SQLException ex) {
            Logger.getLogger(NhanKhauController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }




    public void chuyenNK() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/chuyenNhanKhau.fxml"));
        Parent chinhSuaNKView = loader.load();
        ChuyenNhanKhauController controller = loader.getController();
        NhanKhau selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }
        String trangthai_daMat=selected.getTrangThai();
        if(trangthai_daMat.equals("???? m???t") ){
            Alert m2 = new Alert(Alert.AlertType.INFORMATION);
            m2.setTitle("Th??ng b??o!");
            m2.setHeaderText("Kh??ng th??? th???c hi???n ch???c n??ng do ng?????i n??y ???? qua ?????i.");

            m2.show();
            return;

        }else{
            controller.setChuyenNhanKhau(selected);
            Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("CHUY???N NH??N KH???U");
            Scene scene = new Scene(chinhSuaNKView);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void tamVang() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/tamVang.fxml"));
        Parent tamVangNK = loader.load();
        QuanLyTamVangController controller = loader.getController();
        NhanKhau selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }
        String trangthai_daMat=selected.getTrangThai();
        if(trangthai_daMat.equals("???? m???t") ){
            Alert m2 = new Alert(Alert.AlertType.INFORMATION);
            m2.setTitle("Th??ng b??o!");
            m2.setHeaderText("Kh??ng th??? th???c hi???n ch???c n??ng do ng?????i n??y ???? qua ?????i.");

            m2.show();
            return;

        }else{
            controller.setTamVang(selected);
            Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("QU???N L?? T???M V???NG");
            Scene scene = new Scene(tamVangNK);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void khaiTu() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/khaiTu.fxml"));
        Parent khaiTuNK = loader.load();
        KhaiTuController controller = loader.getController();
        NhanKhau selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }
        String trangthai_daMat=selected.getTrangThai();
        if(trangthai_daMat.equals("???? m???t") ){
            Alert m2 = new Alert(Alert.AlertType.INFORMATION);
            m2.setTitle("Th??ng b??o!");
            m2.setHeaderText("Kh??ng th??? th???c hi???n ch???c n??ng do ng?????i n??y ???? qua ?????i.");

            m2.show();
            return;

        }else{
            controller.setKhaiTu(selected);
            Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("KHAI T???");
            Scene scene = new Scene(khaiTuNK, 1100, 700);
            stage.setScene(scene);
            stage.show();
        }

    }

    public void tamTru() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/nhanKhau/tamTru.fxml"));
        Parent tamTruNK = loader.load();
        TamTruController controller = loader.getController();
        NhanKhau selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert m = new Alert(Alert.AlertType.INFORMATION);
            m.setTitle("Th??ng b??o!");
            m.setHeaderText("Kh??ng nh??n kh???u n??o ???????c ch???n.");
            m.setContentText("Vui l??ng ch???n l???i.");
            m.show();
            return;
        }
        String trangthai_daMat=selected.getTrangThai();
        if(trangthai_daMat.equals("???? m???t") ){
            Alert m2 = new Alert(Alert.AlertType.INFORMATION);
            m2.setTitle("Th??ng b??o!");
            m2.setHeaderText("Kh??ng th??? th???c hi???n ch???c n??ng do ng?????i n??y ???? qua ?????i.");

            m2.show();
            return;

        }else{
            controller.setTamTru(selected);
            Stage stage = new Stage();
//        stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("QU???N L?? T???M TR??");
            Scene scene = new Scene(tamTruNK);
            stage.setScene(scene);
            stage.show();
        }

    }
}

