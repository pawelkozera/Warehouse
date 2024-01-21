package org.warehouse.OperationStrategyFactory;

import org.warehouse.OperationStrategies.*;
import org.warehouse.OperationStrategies.Commodity.GetCommodityByIdStrategy;
import org.warehouse.OperationStrategies.Commodity.ListCommoditiesStrategy;
import org.warehouse.OperationStrategies.CommodityReception.AddCommodityReceptionRecordStrategy;
import org.warehouse.OperationStrategies.CommodityReception.AddCommodityStockStrategy;
import org.warehouse.OperationStrategies.CommodityReception.AddCommodityToSellReceptionStrategy;
import org.warehouse.OperationStrategies.CommodityRelease.AddCommodityReleaseStrategy;
import org.warehouse.OperationStrategies.CommodityRelease.SubstractCommodityStockStrategy;
import org.warehouse.OperationStrategies.CommodityRelease.SubstractCommodityToSellStrategy;
import org.warehouse.OperationStrategies.Employee.*;
import org.warehouse.OperationStrategies.Invoice.*;
import org.warehouse.OperationStrategies.InvoiceCommodities.AddInvoiceCommodityStrategy;
import org.warehouse.OperationStrategies.Reception.AddReceptionStrategy;
import org.warehouse.OperationStrategies.Reception.ListReceptionIdStrategy;
import org.warehouse.OperationStrategies.Release.AddReleaseStrategy;

public class ConcreteOperationStrategyFactory implements OperationStrategyFactory {
    @Override
    public <T> OperationStrategy<T> createStrategy(String operationCode) {
        switch (operationCode) {
            case "ADD_EMPLOYEE":
                return (OperationStrategy<T>) new AddEmployeeStrategy();
            case "LIST_EMPLOYEES":
                return (OperationStrategy<T>) new ListEmployeesStrategy();
            case "LIST_LOGINS_NAMES":
                return (OperationStrategy<T>) new ListLoginsAndNamesStrategy();
            case "GET_EMPLOYEE_BY_LOGIN":
                return (OperationStrategy<T>) new GetEmployeeByLoginStrategy();
            case "EDIT_EMPLOYEE":
                return (OperationStrategy<T>) new EditEmployeeStrategy();
            case "LIST_COMMODITIES":
                return (OperationStrategy<T>) new ListCommoditiesStrategy();
            case "GET_COMMODITY_BY_ID":
                return (OperationStrategy<T>) new GetCommodityByIdStrategy();
            case "LIST_INVOICES":
                return (OperationStrategy<T>) new ListInvoicesStrategy();
            case "LIST_UNRELEASED_INVOICES":
                return (OperationStrategy<T>) new ListUnreleasedInvoicesStrategy();
            case "UPDATE_INVOICE_STATUS":
                return (OperationStrategy<T>) new UpdateInvoiceStatusStrategy();
            case "ADD_RELEASE":
                return (OperationStrategy<T>) new AddReleaseStrategy();
            case "ADD_COMMODITY_RELEASE":
                return (OperationStrategy<T>) new AddCommodityReleaseStrategy();
            case "SUBSTRACT_COMMODITY_STOCK":
                return (OperationStrategy<T>) new SubstractCommodityStockStrategy();
            case "SUBSTRACT_COMMODITY_TO_SELL":
                return (OperationStrategy<T>) new SubstractCommodityToSellStrategy();
            case "ADD_COMMODITY_TO_SELL":
                return (OperationStrategy<T>) new AddCommodityToSellReceptionStrategy();
            case "ADD_COMMODITY_STOCK":
                return (OperationStrategy<T>) new AddCommodityStockStrategy();
            case "ADD_RECEPTION":
                return (OperationStrategy<T>) new AddReceptionStrategy();
            case "ADD_COMMODITY_RECEPTION":
                return (OperationStrategy<T>) new AddCommodityReceptionRecordStrategy();
            case "GET_ALL_RECEPTION_ID" :
                return (OperationStrategy<T>) new ListReceptionIdStrategy();
            case "LOGIN":
                return (OperationStrategy<T>) new LoginStrategy();
            case "INSERT_INVOICE" :
                return (OperationStrategy<T>) new AddInvoiceStrategy();
            case "INSERT_INVOICE_COMMODITY":
                return (OperationStrategy<T>) new AddInvoiceCommodityStrategy();
            case "GET_INVOICE_STATS":
                return (OperationStrategy<T>) new ListInvoiceStatsStrategy();
            default:
                throw new IllegalArgumentException("Unkown code operation: " + operationCode);
        }
    }
}
