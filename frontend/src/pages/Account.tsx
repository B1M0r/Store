
import { useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { fetchAccounts, createAccount, updateAccount, deleteAccount } from '@/services/api';
import { Account as AccountType } from '@/types';
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
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { toast } from '@/components/ui/use-toast';
import { Plus, Edit, Trash2, Layers } from 'lucide-react';
import AccountDetails from '@/components/AccountDetails';

const Account = () => {
  const queryClient = useQueryClient();
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [formDialogOpen, setFormDialogOpen] = useState(false);
  const [detailsDialogOpen, setDetailsDialogOpen] = useState(false);
  const [currentAccount, setCurrentAccount] = useState<AccountType | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState<Partial<AccountType>>({
    nickname: '',
    firstName: '',
    lastName: '',
    email: ''
  });

  const { data: accounts = [], isLoading } = useQuery({
    queryKey: ['accounts'],
    queryFn: fetchAccounts,
  });

  const handleDelete = (account: AccountType) => {
    setCurrentAccount(account);
    setDeleteDialogOpen(true);
  };

  const handleEdit = (account: AccountType) => {
    setCurrentAccount(account);
    setFormData({
      nickname: account.nickname,
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email
    });
    setIsEditMode(true);
    setFormDialogOpen(true);
  };

  const handleViewDetails = (account: AccountType) => {
    setCurrentAccount(account);
    setDetailsDialogOpen(true);
  };

  const handleCreate = () => {
    setCurrentAccount(null);
    setFormData({
      nickname: '',
      firstName: '',
      lastName: '',
      email: ''
    });
    setIsEditMode(false);
    setFormDialogOpen(true);
  };

  const handleFormSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isEditMode && currentAccount?.id) {
        await updateAccount(currentAccount.id, { ...formData, id: currentAccount.id } as AccountType);
        toast({
          title: "Аккаунт обновлен",
          description: "Аккаунт был успешно обновлен.",
        });
      } else {
        await createAccount(formData as AccountType);
        toast({
          title: "Аккаунт создан",
          description: "Аккаунт был успешно создан.",
        });
      }
      queryClient.invalidateQueries({ queryKey: ['accounts'] });
      setFormDialogOpen(false);
    } catch (error) {
      toast({
        title: "Ошибка",
        description: isEditMode ? "Не удалось обновить аккаунт." : "Не удалось создать аккаунт.",
        variant: "destructive",
      });
    }
  };

  const handleDeleteSubmit = async () => {
    if (!currentAccount?.id) return;

    try {
      await deleteAccount(currentAccount.id);
      queryClient.invalidateQueries({ queryKey: ['accounts'] });
      toast({
        title: "Аккаунт удален",
        description: "Аккаунт был успешно удален.",
      });
      setDeleteDialogOpen(false);
    } catch (error) {
      toast({
        title: "Ошибка",
        description: "Не удалось удалить аккаунт.",
        variant: "destructive",
      });
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold">Аккаунты</h1>
          <Button
              onClick={handleCreate}
              className="bg-blue-600 hover:bg-blue-700"
          >
            <Plus className="mr-2 h-4 w-4" /> Создать аккаунт
          </Button>
        </div>

        {isLoading ? (
            <div className="text-center p-8">
              <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-blue-600 border-r-transparent"></div>
              <p className="mt-2">Загрузка аккаунтов...</p>
            </div>
        ) : (
            <div className="rounded-md border shadow-sm overflow-hidden bg-white">
              <Table>
                <TableHeader className="bg-gray-50">
                  <TableRow>
                    <TableHead className="font-semibold">Никнейм</TableHead>
                    <TableHead className="font-semibold">Имя</TableHead>
                    <TableHead className="font-semibold">Фамилия</TableHead>
                    <TableHead className="font-semibold">Email</TableHead>
                    <TableHead className="w-[100px]">Подробнее</TableHead>
                    <TableHead className="w-[120px]">Действия</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {accounts.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={6} className="text-center py-8 text-gray-500">
                          Аккаунты не найдены
                        </TableCell>
                      </TableRow>
                  ) : (
                      accounts.map((account) => (
                          <TableRow key={account.id} className="hover:bg-gray-50">
                            <TableCell className="font-medium">{account.nickname}</TableCell>
                            <TableCell>{account.firstName}</TableCell>
                            <TableCell>{account.lastName}</TableCell>
                            <TableCell>{account.email}</TableCell>
                            <TableCell>
                              <Button
                                  variant="outline"
                                  size="sm"
                                  onClick={() => handleViewDetails(account)}
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
                                    onClick={() => handleEdit(account)}
                                    className="border-gray-300"
                                >
                                  <Edit className="h-4 w-4" />
                                </Button>
                                <Button
                                    variant="destructive"
                                    size="sm"
                                    onClick={() => handleDelete(account)}
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

        {/* Form Dialog */}
        <Dialog open={formDialogOpen} onOpenChange={setFormDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>{isEditMode ? 'Редактировать аккаунт' : 'Создать аккаунт'}</DialogTitle>
            </DialogHeader>

            <form onSubmit={handleFormSubmit} className="space-y-4 py-4">
              <div className="space-y-2">
                <Label htmlFor="nickname">Никнейм</Label>
                <Input
                    id="nickname"
                    name="nickname"
                    value={formData.nickname}
                    onChange={handleInputChange}
                    required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="firstName">Имя</Label>
                <Input
                    id="firstName"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleInputChange}
                    required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="lastName">Фамилия</Label>
                <Input
                    id="lastName"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleInputChange}
                    required
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                    id="email"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    required
                />
              </div>

              <DialogFooter>
                <Button variant="outline" type="button" onClick={() => setFormDialogOpen(false)}>Отмена</Button>
                <Button type="submit">{isEditMode ? 'Сохранить' : 'Создать'}</Button>
              </DialogFooter>
            </form>
          </DialogContent>
        </Dialog>

        {/* Delete Confirmation Dialog */}
        <Dialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Подтвердите удаление</DialogTitle>
              <DialogDescription>
                Вы действительно хотите удалить аккаунт {currentAccount?.firstName} {currentAccount?.lastName}? Это действие нельзя отменить.
              </DialogDescription>
            </DialogHeader>

            <DialogFooter>
              <Button variant="outline" onClick={() => setDeleteDialogOpen(false)}>Отмена</Button>
              <Button variant="destructive" onClick={handleDeleteSubmit}>Удалить</Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>

        {/* Account Details Dialog with Relations */}
        {currentAccount && (
            <AccountDetails
                account={currentAccount}
                open={detailsDialogOpen}
                onClose={() => setDetailsDialogOpen(false)}
            />
        )}
      </div>
  );
};

export default Account;
