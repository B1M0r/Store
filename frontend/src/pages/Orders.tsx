
import { useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { fetchOrders, fetchProducts, deleteOrder, createOrder, updateOrder } from '@/services/api';
import { Order as OrderType, Product } from '@/types';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from '@/components/ui/dialog';
import { toast } from '@/components/ui/use-toast';
import { Plus, Edit, Trash2, Layers } from 'lucide-react';
import OrderForm from '@/components/OrderForm';
import OrderDetails from '@/components/OrderDetails';

const Orders = () => {
  const queryClient = useQueryClient();
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [formDialogOpen, setFormDialogOpen] = useState(false);
  const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
  const [currentOrder, setCurrentOrder] = useState<OrderType | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);

  const { data: orders = [], isLoading: ordersLoading } = useQuery({
    queryKey: ['orders'],
    queryFn: fetchOrders,
  });

  const { data: products = [] } = useQuery({
    queryKey: ['products'],
    queryFn: () => fetchProducts(),
  });

  const handleDelete = (order: OrderType) => {
    setCurrentOrder(order);
    setDeleteDialogOpen(true);
  };

  const handleEdit = (order: OrderType) => {
    setCurrentOrder(order);
    setIsEditMode(true);
    setFormDialogOpen(true);
  };

  const handleViewDetails = (order: OrderType) => {
    setCurrentOrder(order);
    setDetailsDialogOpen(true);
  };

  const handleCreate = () => {
    setCurrentOrder(null);
    setIsEditMode(false);
    setFormDialogOpen(true);
  };

  const handleDeleteSubmit = async () => {
    if (!currentOrder?.id) return;

    try {
      await deleteOrder(currentOrder.id);

      queryClient.invalidateQueries({ queryKey: ['orders'] });
      toast({
        title: "Заказ удален",
        description: "Заказ был успешно удален.",
      });
      setDeleteDialogOpen(false);
    } catch (error) {
      toast({
        title: "Ошибка",
        description: "Не удалось удалить заказ.",
        variant: "destructive",
      });
    }
  };

  const handleFormSubmit = async (orderData: OrderType) => {
    try {
      if (isEditMode && currentOrder?.id) {
        await updateOrder(currentOrder.id, orderData);
        toast({
          title: "Заказ обновлен",
          description: "Заказ был успешно обновлен.",
        });
      } else {
        await createOrder(orderData);
        toast({
          title: "Заказ создан",
          description: "Заказ был успешно создан.",
        });
      }
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      setFormDialogOpen(false);
    } catch (error) {
      toast({
        title: "Ошибка",
        description: isEditMode
            ? "Не удалось обновить заказ."
            : "Не удалось создать заказ.",
        variant: "destructive",
      });
    }
  };

  // Helper function to format date
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleString();
  };

  // Helper function to get product names
  const getProductNames = (orderProducts?: Product[]) => {
    if (!orderProducts || orderProducts.length === 0) return "Нет продуктов";

    return orderProducts
        .map(p => p.name)
        .join(", ");
  };

  return (
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold">Заказы</h1>
          <Button
              onClick={handleCreate}
              className="bg-blue-600 hover:bg-blue-700"
          >
            <Plus className="mr-2 h-4 w-4" /> Создать заказ
          </Button>
        </div>

        {ordersLoading ? (
            <div className="text-center p-8">
              <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-blue-600 border-r-transparent"></div>
              <p className="mt-2">Загрузка заказов...</p>
            </div>
        ) : (
            <div className="rounded-md border shadow-sm overflow-hidden bg-white">
              <Table>
                <TableHeader className="bg-gray-50">
                  <TableRow>
                    <TableHead className="font-semibold">ID заказа</TableHead>
                    <TableHead className="font-semibold">Дата</TableHead>
                    <TableHead className="font-semibold">Продукты</TableHead>
                    <TableHead className="text-right font-semibold">Сумма</TableHead>
                    <TableHead className="w-[100px]">Подробнее</TableHead>
                    <TableHead className="w-[100px]">Действия</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {orders.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={6} className="text-center py-8 text-gray-500">
                          Заказы не найдены
                        </TableCell>
                      </TableRow>
                  ) : (
                      orders.map((order) => (
                          <TableRow key={order.id} className="hover:bg-gray-50">
                            <TableCell className="font-medium">#{order.id}</TableCell>
                            <TableCell>{formatDate(order.orderDate)}</TableCell>
                            <TableCell>{getProductNames(order.products)}</TableCell>
                            <TableCell className="text-right font-medium">{order.totalPrice.toFixed(2)} $</TableCell>
                            <TableCell>
                              <Button
                                  variant="outline"
                                  size="sm"
                                  onClick={() => handleViewDetails(order)}
                                  className="border-gray-300"
                              >
                                <Layers className="h-4 w-4" />
                              </Button>
                            </TableCell>
                            <TableCell>
                              <div className="flex space-x-2">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={() => handleEdit(order)}
                                    className="border-gray-300"
                                >
                                  <Edit className="h-4 w-4" />
                                </Button>
                                <Button
                                    variant="destructive"
                                    size="sm"
                                    onClick={() => handleDelete(order)}
                                >
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </div>
                            </TableCell>
                          </TableRow>
                      ))
                  )}
                </TableBody>
              </Table>
            </div>
        )}

        {/* Delete Confirmation Dialog */}
        <Dialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Подтвердите удаление</DialogTitle>
              <DialogDescription>
                Вы действительно хотите удалить заказ #{currentOrder?.id}? Это действие нельзя отменить.
              </DialogDescription>
            </DialogHeader>

            <DialogFooter>
              <Button variant="outline" onClick={() => setDeleteDialogOpen(false)}>Отмена</Button>
              <Button variant="destructive" onClick={handleDeleteSubmit}>Удалить</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        {/* Create/Edit Order Dialog */}
        <Dialog open={formDialogOpen} onOpenChange={setFormDialogOpen}>
          <DialogContent className="max-w-md">
            <DialogHeader>
              <DialogTitle>{isEditMode ? 'Редактировать заказ' : 'Создать заказ'}</DialogTitle>
            </DialogHeader>

            <OrderForm
                order={currentOrder}
                onSubmit={handleFormSubmit}
                products={products}
                isEditMode={isEditMode}
            />
          </DialogContent>
        </Dialog>

        {/* Order Details Dialog with Relations */}
        {currentOrder && (
            <OrderDetails
                order={currentOrder}
                open={detailsDialogOpen}
                onClose={() => setDetailsDialogOpen(false)}
            />
        )}
      </div>
  );
};

export default Orders;
