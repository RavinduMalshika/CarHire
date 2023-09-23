package lk.ijse.carhire.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.carhire.dao.custom.CarModelDao;
import lk.ijse.carhire.entity.CarModelEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CarModelDaoImpl implements CarModelDao {
    @Override
    public boolean save(CarModelEntity carModelEntity, Session session) {
        try {
            String model = (String) session.save(carModelEntity);
            if(model != null) {
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
    public boolean update(CarModelEntity carModelEntity, Session session) {
        try {
            session.update(carModelEntity);
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public boolean update(CarModelEntity carModelEntity, Session session, String previousId) {
        try {
            String hql = "UPDATE CarModelEntity m SET m.model = :model, m.carBrandEntity = :brand, m.carCategoryEntity = :category, m.createdBy = :createdBy, m.updatedBy = :updatedBy WHERE m.model = :previousId";
            Query query = session.createQuery(hql);
            query.setParameter("model", carModelEntity.getModel());
            query.setParameter("brand", carModelEntity.getCarBrandEntity());
            query.setParameter("category", carModelEntity.getCarCategoryEntity());
            query.setParameter("createdBy", carModelEntity.getCreatedBy());
            query.setParameter("updatedBy", carModelEntity.getUpdatedBy());
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
            CarModelEntity carModelEntity = session.get(CarModelEntity.class, s);
            session.delete(carModelEntity);
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public CarModelEntity get(String s, Session session) {
        CarModelEntity carModelEntity = session.get(CarModelEntity.class, s);
        return carModelEntity;
    }

    @Override
    public List<CarModelEntity> getAll(Session session) {
        String hql = "FROM CarModelEntity";
        Query query = session.createQuery(hql);
        List<CarModelEntity> carModelEntities = query.list();
        return carModelEntities;
    }
}
