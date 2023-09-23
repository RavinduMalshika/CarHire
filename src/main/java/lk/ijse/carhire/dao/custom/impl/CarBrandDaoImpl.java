package lk.ijse.carhire.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.carhire.dao.custom.CarBrandDao;
import lk.ijse.carhire.entity.CarBrandEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CarBrandDaoImpl implements CarBrandDao {
    @Override
    public boolean save(CarBrandEntity carBrandEntity, Session session) {
        try {
            String brand = (String) session.save(carBrandEntity);
            if(brand != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }

    }

    @Override
    public boolean update(CarBrandEntity carBrandEntity, Session session) {
        try {
            session.update(carBrandEntity);
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public boolean update(CarBrandEntity carBrandEntity, Session session, String previousId) {
        try {
            String hql = "UPDATE CarBrandEntity b SET b.brand = :brand, b.createdBy = :createdBy, b.updatedBy = :updatedBy WHERE b.brand = :previousId";
            Query query = session.createQuery(hql);
            query.setParameter("brand", carBrandEntity.getBrand());
            query.setParameter("createdBy", carBrandEntity.getCreatedBy());
            query.setParameter("updatedBy", carBrandEntity.getUpdatedBy());
            query.setParameter("previousId", previousId);
            return query.executeUpdate() > 0;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public boolean delete(String s, Session session) {
        try {
            CarBrandEntity carBrandEntity = session.get(CarBrandEntity.class, s);
            session.delete(carBrandEntity);
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public CarBrandEntity get(String s, Session session) {
        CarBrandEntity carBrandEntity = session.get(CarBrandEntity.class, s);
        return carBrandEntity;
    }

    @Override
    public List<CarBrandEntity> getAll(Session session) {
        String hql = "FROM CarBrandEntity";
        Query query = session.createQuery(hql);
        List<CarBrandEntity> carBrandEntities = query.list();
        return carBrandEntities;
    }
}
