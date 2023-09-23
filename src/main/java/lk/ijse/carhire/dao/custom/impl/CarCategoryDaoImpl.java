package lk.ijse.carhire.dao.custom.impl;

import javafx.scene.control.Alert;
import lk.ijse.carhire.dao.custom.CarCategoryDao;
import lk.ijse.carhire.entity.CarCategoryEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CarCategoryDaoImpl implements CarCategoryDao {
    @Override
    public boolean save(CarCategoryEntity carCategoryEntity, Session session) {
        try {
            String category = (String) session.save(carCategoryEntity);
            if( category != null) {
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
    public boolean update(CarCategoryEntity carCategoryEntity, Session session) {
        try {
            session.update(carCategoryEntity);
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public boolean update(CarCategoryEntity carCategoryEntity, Session session, String previousId) {
        try {
            String hql = "UPDATE CarCategoryEntity c SET c.category = :category, c.createdBy = :createdBy, c.updatedBy = :updatedBy WHERE c.category = :previousId";
            Query query = session.createQuery(hql);
            query.setParameter("category", carCategoryEntity.getCategory());
            query.setParameter("createdBy", carCategoryEntity.getCreatedBy());
            query.setParameter("updatedBy", carCategoryEntity.getUpdatedBy());
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
            CarCategoryEntity carCategoryEntity = session.get(CarCategoryEntity.class, s);
            session.delete(carCategoryEntity);
            return true;
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            return false;
        }
    }

    @Override
    public CarCategoryEntity get(String s, Session session) {
        CarCategoryEntity carCategoryEntity = session.get(CarCategoryEntity.class, s);
        return carCategoryEntity;
    }

    @Override
    public List<CarCategoryEntity> getAll(Session session) {
        String hql = "FROM CarCategoryEntity";
        Query query = session.createQuery(hql);
        List<CarCategoryEntity> carCategoryEntities = query.list();
        return carCategoryEntities;
    }
}
